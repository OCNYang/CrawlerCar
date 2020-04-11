package org.example.pageprocessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.SpiderManager;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2020/4/11 12:45 下午.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class CarTypePageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        page.putField("json", page.getRawText());

        String url = page.getUrl().get();

        String brand_id_3 = url.substring(url.lastIndexOf("serialid=") + 9, url.lastIndexOf("&mb="));
        String brand_id_1 = url.substring(url.lastIndexOf("&mb=") + 4, url.lastIndexOf("&cb="));
        String brand_id_2 = url.substring(url.lastIndexOf("&cb=") + 4, url.lastIndexOf("&letter="));
        String letter = url.substring(url.lastIndexOf("&letter=") + 8);

        JSONArray jsonArray = JSON.parseArray(page.getRawText());
        if (jsonArray != null && jsonArray.size() != 0) {
            StringBuilder ids = new StringBuilder(jsonArray.getJSONObject(0).getString("Car_ID"));
            for (int i = 1; i < jsonArray.size(); i++) {
                ids.append(",").append(jsonArray.getJSONObject(i).getString("Car_ID"));
            }

            SpiderManager.getCarTypesDetails(ids.toString(), letter, brand_id_1, brand_id_2, brand_id_3);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
