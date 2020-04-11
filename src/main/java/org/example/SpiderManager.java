package org.example;

import org.example.pageprocessor.CarBrandPageProcessor;
import org.example.pageprocessor.CarDetailParametersPageProcessor;
import org.example.pageprocessor.CarTypePageProcessor;
import org.example.pipeline.CarBrandJsonFilePipeline;
import org.example.pipeline.CarDetailParametersJsonFilePipeline;
import org.example.pipeline.CarTypeJsonFilePipeline;
import us.codecraft.webmagic.Spider;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2020/4/11 10:20 上午.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class SpiderManager {
    public static final String URL_START = "http://api.car.bitauto.com/CarInfo/getlefttreejson.ashx?tagtype=chexing&pagetype=masterbrand&objid=0";

    /**
     * 获取 所有车型品牌
     */
    public static void getCarBrands() {
        Spider.create(new CarBrandPageProcessor())
                .addUrl(URL_START)
                .setDownloader(new HttpClientDownloader())
//                .addPipeline(new ConsolePipeline())
                .addPipeline(new CarBrandJsonFilePipeline())
                .thread(5)
                .run();
    }

    /**
     * 获取 具体某款车的不同配置型号
     *
     * @param id
     * @param letter
     * @param mb
     * @param cb
     */
    public static void getCarTypes(String id, String letter, String mb, String cb) {
        Spider.create(new CarTypePageProcessor())
                .addUrl("https://wxs.app.yiche.com/queryparam.ashx?serialid=" + id + "&mb=" + mb + "&cb=" + cb + "&letter=" + letter)
                .setDownloader(new HttpClientDownloader())
//                .addPipeline(new ConsolePipeline())
                .addPipeline(new CarTypeJsonFilePipeline())
                .thread(5)
                .run();
    }

    public static void getCarTypesDetails(String ids, String letter, String mb, String cb, String cs) {
        Spider.create(new CarDetailParametersPageProcessor())
                .addUrl("http://api.app.yiche.com/webapi/api.ashx?method=carstyle.detail&v=1&carid=" + ids + "&cs=" + cs + "&mb=" + mb + "&cb=" + cb + "&letter=" + letter)
                .setDownloader(new HttpClientDownloader())
//                .addPipeline(new ConsolePipeline())
                .addPipeline(new CarDetailParametersJsonFilePipeline())
                .thread(5)
                .run();
    }
}
