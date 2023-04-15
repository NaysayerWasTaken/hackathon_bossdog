package main

import (
	"log"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type DataPoint struct {
	Time  time.Time `json:time`
	Value float64   `json:value`
}

func main() {
	r := gin.Default()
	r.POST("/dplist", func(c *gin.Context) {
		var dps []DataPoint
		c.ShouldBindJSON(&dps)
		log.Println(dps)
		c.JSON(http.StatusOK, gin.H{
			"message": "success",
		})
	})
	r.Run() // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
