### Base64
        Base64.NO_PADDING： 参数是省略编码字符串最后的"="
        Base64.NO_WRAP: 省略所有的换行符（设置后CRLF就没有了）    
        Base64.DEFAULT: 使用默认的的方法编码
        Base64.CRLF:    使用CR LF这一对作为一行的结尾而不是Unix风格的LF
        Base64.URL_SAFE: 编码时不使用对URL和文件名有特殊意义的字符来作为编码字符，具体就是以-和_取代+和/    

#### 遇到的问题

1、为什么使用PKCS7Padding加密时没有问题，使用PKCS5Padding却出现了问题。  （CryptoUtils.java）
   错误信息“Attempt to get length of null array”。

2、为什么加密不指定向量，解密却要指定向量？（CryptoUtils.java）

3、错误信息Caller-provided IV not permitted，加密时指定向量（CryptoUtils.java）
  原因：AndroidKeyStore类型的provider不允许提供向量

4、java.security.InvalidKeyException: IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.
  原因：解密没有指定向量。