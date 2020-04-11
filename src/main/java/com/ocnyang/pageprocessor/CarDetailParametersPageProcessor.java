package com.ocnyang.pageprocessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2020/4/11 2:59 下午.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class CarDetailParametersPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        page.putField("json", page.getRawText());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
