package webprobe;

import webprobe.enums.BrowserType;
import webprobe.enums.RemoteBrowserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pleshivtsevsv on 27.12.2017.
 */
public class WebProbesPool {

    List<WebProbe> webProbes;

//**************************** Singleton   *******************************************************************************
    private static WebProbesPool instance;

    private WebProbesPool(){
        webProbes = new ArrayList<WebProbe>();
    }

    public static synchronized WebProbesPool getInstance(){
        if (instance == null){
            instance = new WebProbesPool();
        }
        return  instance;
    }

//***********************************************************************************************************
    public WebProbe getWebProbe(BrowserType browserType){
        for (WebProbe webProbe: webProbes){
            if (!webProbe.isBusy()){
                webProbe.capture();
                return webProbe;
            }
        }

        WebProbe webProbe = new WebProbe(browserType);
        webProbe.capture();
        webProbes.add(webProbe);
        return webProbe;
    }

    public WebProbe getWebProbe(String gridUrl, RemoteBrowserType remoteBrowserType){
        for (WebProbe webProbe: webProbes){
            if (!webProbe.isBusy()){
                webProbe.capture();
                return webProbe;
            }
        }

        WebProbe webProbe = new WebProbe(gridUrl, remoteBrowserType);
        webProbe.capture();
        webProbes.add(webProbe);
        return webProbe;
    }



    public void quitAll(){
        webProbes.forEach(webProbe -> webProbe.stop());
        webProbes.clear();
    }

}