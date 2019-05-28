package webprobe;

import webprobe.enums.BrowserType;
import webprobe.enums.RemoteBrowserType;
import webprobe.utils.Assert;

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
    public WebProbe addWebProbeToPool(BrowserType browserType, String webProbeName){
        WebProbe webProbe = new WebProbe(browserType);
        webProbe.setName(webProbeName);
        webProbe.capture();
        webProbes.add(webProbe);
        return webProbe;
    }

    public WebProbe addWebProbeToPool(String gridUrl, RemoteBrowserType remoteBrowserType, String webProbeName){
        WebProbe webProbe = new WebProbe(gridUrl, remoteBrowserType);
        webProbe.setName(webProbeName);
        webProbe.capture();
        webProbes.add(webProbe);
        return webProbe;
    }

    public WebProbe getWebProbeByName(String webProbeName){
        for (WebProbe webProbe: webProbes){
            if (webProbe.getName().equals(webProbeName)){
                return webProbe;
            }
        }
        Assert.pageAssert("*** WebProbe " + webProbeName + " not found");
        return null;
    }

    public void quitByName(String webProbeName){
        for (WebProbe webProbe: webProbes){
            if (webProbe.getName().equals(webProbeName)){
                webProbe.stop();
                webProbe.setName("*** Stopped, do not use!");
                return;
            }
        }
        Assert.pageAssert("*** Can't stop WebProbe " + webProbeName);
    }

    public List<WebProbe> getWebProbes(){
        return webProbes;
    }

    public void quitAll(){
        webProbes.forEach(webProbe -> webProbe.stop());
        webProbes.clear();
    }

}