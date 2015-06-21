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

        WebDriver driver = new FirefoxDriver();

        // Going to some XSS-having website saved on the web archive
        driver.get("https://web.archive.org/web/20060819041322/http://tarcima.narod.ru/otherencoding.htm");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        WebElement textarea=driver.findElement(By.xpath("//textarea"));
        WebElement button=driver.findElement(By.xpath("//input[@type='button']"));

        LimitedRandomImageResourceGenerator tg=new LimitedRandomImageResourceGenerator();
        tg.setStringLength(3);
        tg.setFileLocation("bug.jpg");
        tg.setExt("jpg");
        XSSCheck x= XSSVerifier.createNewCheck(4567, XSSTypes.JAVSCRIPT_IMG_TAG.toString(), tg);
/* 4567 is a port number where the XSS checker's embbedded http server runs,  resource generator creates random resource + specified extention and define what should be returned by the server when the XSS succeed*/

        textarea.sendKeys(x.getXSS());
        button.click();
//the WebDriver opens the browser and inserts the injection to the textearea; then the page is transformed to the injected one and the XSS calls the XSS sniffer right in the WebDriver-driven browser

//the XSS host and resource were requested but not nessessarily some parameter was passed
//to ensure it works open localhost:4567/xss.gif?sometext in your browser

        while (!x.getXSSURLCalledStatus())
        {
            Thread.sleep(1000);
        }
        
//the XSS host and resource were requested and some parameter was passed but that was not the hijacked cookie
//to ensure it works open localhost:4567/xss.gif?visited=02022015 in your browser

        while (!x.getXSSCookiePassedStatus())
        {
            Thread.sleep(1000);
        }

//the XSS host and resource were requested, some parameter was passed and this parameter is cookie named visited
//so the particular cookie was hijacked and that was verified

        while (!x.getXSSCookieHijacked("visited"))
        {
            Thread.sleep(1000);
        }

//the cookie never set by the website will be never requested
        while (!x.getXSSCookieHijacked("visited2"))
        {
            Thread.sleep(1000);
        }

```

##Notes
The xssverifier library doesn't check availability of the ports on your local machine. You should ensure that port is not already bint before creating the sniffer.
