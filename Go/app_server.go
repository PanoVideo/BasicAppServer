package Go

import (
	"bytes"
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/gin-gonic/gin"
	"io/ioutil"
	"log"
	"math/rand"
	"net/http"
	"time"
)

type AppLoginRequest struct {
	AppId           string `json:"appId" binding:"required"`
	ChannelId       string `json:"ChannelId" binding:"required"`
	UserId          string `json:"UserId"`
	Privileges      uint   `json:"Privileges"`
	Duration        uint   `json:"Duration"`
	ChannelDuration uint   `json:"ChannelDuration"`
}

type PanoTokenRequest struct {
	ChannelId       string `json:"channelId" binding:"required"`
	UserId          string `json:"userId"`
	Privileges      uint   `json:"privileges"`
	Duration        uint   `json:"duration"`
	ChannelDuration uint   `json:"channelDuration"`
}

type SdkToken struct {
	Token string `json:token`
}

func main() {
	//put your appSecret here
	appSecret := "xxx"

	const PANO_TOKEN_URL = "https://api.pano.video/auth/token"

	router := gin.Default()

	router.POST("/app/login", func(c *gin.Context) {

		var req AppLoginRequest

		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}
		postBody := PanoTokenRequest{
			ChannelId:       req.ChannelId,
			UserId:          req.UserId,
			Privileges:      req.Privileges,
			Duration:        req.Duration,
			ChannelDuration: req.ChannelDuration,
		}
		b, err := json.Marshal(postBody)
		if err != nil {
			log.Print("json format err:", err)
			return
		}
		client := &http.Client{}
		body := bytes.NewBuffer(b)
		tokenReq, err := http.NewRequest("POST", PANO_TOKEN_URL, body)
		if err != nil {
			log.Print("get token from pano cloud err:", err)
			return
		}
		tokenReq.Header.Set("Content-Type", "application/json")
		tokenReq.Header.Set("Authorization", "PanoSign "+generatePanoSign(req.AppId, appSecret))
		rand.Seed(time.Now().UnixNano())
		tokenReq.Header.Set("Tracking-Id", string(rand.Intn(100000)))

		tokenRes, err := client.Do(tokenReq)
		defer tokenRes.Body.Close()
		content, err := ioutil.ReadAll(tokenRes.Body)
		if err != nil {
			log.Print("read pano token err:", err)
			return
		}
		var sdkTokenRes = &SdkToken{}
		err = json.Unmarshal(content, sdkTokenRes)
		c.String(http.StatusOK, sdkTokenRes.Token)
	})
	router.Run() // listen and serve on 0.0.0.0:8080 (for "localhost:8080")
}

func signature(appId string, timestamp int64, appSecret string) string {
	mac := hmac.New(sha256.New, []byte(appSecret))
	mac.Write([]byte(fmt.Sprintf("%s%d", appId, timestamp)))
	return base64.StdEncoding.EncodeToString(mac.Sum(nil))
}

func generatePanoSign(appId string, appSecret string) string {
	current := time.Now().Unix()
	return fmt.Sprintf("%s.%d.%s", appId, current, signature(appId, current, appSecret))
}
