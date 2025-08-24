package main

import (
	"be-gogin/internal/wire"
)

func main() {
	server, _ := wire.InitializeServer()
	server.Run()
}
