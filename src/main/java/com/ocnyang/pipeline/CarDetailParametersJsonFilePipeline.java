package com.ocnyang.pipeline;

import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2020/4/11 3:00 下午.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class CarDetailParametersJsonFilePipeline extends JsonFilePipeline {

    public CarDetailParametersJsonFilePipeline() {
        setPath("../data/webmagic");
    }

    public void process(ResultItems resultItems, Task task) {
        try {
            System.out.println("结果：" + resultItems.get("json"));
            String json = resultItems.get("json").toString();

            String url = resultItems.getRequest().getUrl();
            System.out.println("地址：" + url);

            String brand_id_3 = url.substring(url.lastIndexOf("&cs=") + 4, url.lastIndexOf("&mb="));
            String brand_id_1 = url.substring(url.lastIndexOf("&mb=") + 4, url.lastIndexOf("&cb="));
            String brand_id_2 = url.substring(url.lastIndexOf("&cb=") + 4, url.lastIndexOf("&letter="));
            String letter = url.substring(url.lastIndexOf("&letter=") + 8);

            System.out.println("路径：" + letter + "  " + brand_id_1 + "  " + brand_id_2 + "  " + brand_id_3);

            String path = this.path +
                    PATH_SEPERATOR + "api.car.bitauto.com" + //接口名称
                    PATH_SEPERATOR + letter + //首字母名
                    PATH_SEPERATOR + brand_id_1 + //一级品牌ID
                    PATH_SEPERATOR + brand_id_2 +
                    PATH_SEPERATOR + brand_id_3 +
                    PATH_SEPERATOR;


            String jsonPath = path + "carstyle_detail" + ".json";
            if (!(new File(jsonPath).exists())) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(jsonPath)));
                printWriter.write(json);
                printWriter.close();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).warn("write file error", e.getMessage());
            System.out.println("write file error" + e.getMessage());
        }
    }
}
