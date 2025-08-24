package utils

// ReverseString Hàm đảo ngược một string
// hello -> olleh
func ReverseString(str string) string {
	runes := []rune(str)
	n := len(runes)
	for i := 0; i < n/2; i++ {
		// cú pháp gán swap không cần temp value
		runes[i], runes[n-1-i] = runes[n-1-i], runes[i]
	}
	return string(runes)
}
