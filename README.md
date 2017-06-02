# HowTomcatWorks源码及其用户自定义类加载加解密代码
> 详情见ex04 && ex08
> 使用说明：
> classes文件夹时自己创建的文件夹，下面存放的需要加密的类文件；
> classes文件夹下的key.png存储密码的二维码;
> classes文件夹下的flag.txt文件表识类文件是否已经加密过，true表示已经加密，false表示未加密；
> ex04.pyrmont.core下，SimpleContainer里面的默认类加载器（loadClass）改成用户自定义类加载器；
> ex04.pyrmont.encrypt下，EncryptClass主要是加密类文件的操作；
> ex04.pyrmont.loader下，DecryptClassLoader是用户自定义类加载荷解密操作等；
> ex04.pyrmont.QRCode下，MyQRCodeImage是构架二维码时需要创建的对象；
> ex04.pyrmont.startup下，Bootstrap启动项目的主类,里面有加密的多线程；
> ex04.pyrmont.util下，FileUtil读写文件操作，GenerateKey生成密钥并生成二维码；
