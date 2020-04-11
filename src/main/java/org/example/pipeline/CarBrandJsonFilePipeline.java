package org.example.pipeline;

import com.alibaba.fastjson.JSON;
import org.example.SpiderManager;
import org.example.pageprocessor.CarBrandPageProcessor;
import org.example.utils.DownLoadUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2020/4/11 10:14 上午.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class CarBrandJsonFilePipeline extends JsonFilePipeline {
    public CarBrandJsonFilePipeline() {
        setPath("../data/webmagic");
    }

    public void process(ResultItems resultItems, Task task) {

        try {
            String url = resultItems.getRequest().getUrl();
            String path;
            String brand_id_1;
            String json = resultItems.get(CarBrandPageProcessor.BRAND_JSON).toString();

            if (SpiderManager.URL_START.equals(url)) {
                path = this.path +
                        PATH_SEPERATOR + task.getUUID() + //接口名称
                        PATH_SEPERATOR;
                brand_id_1 = "0";
            } else {
                String letter = resultItems.get(CarBrandPageProcessor.BRAND_LETTER).toString();
                brand_id_1 = resultItems.get(CarBrandPageProcessor.BRAND_BRAND_ID_1).toString();
                path = this.path +
                        PATH_SEPERATOR + task.getUUID() + //接口名称
                        PATH_SEPERATOR + letter + //首字母名
                        PATH_SEPERATOR + brand_id_1 + //一级品牌ID
                        PATH_SEPERATOR;

//                try {
//                    // 下载品牌图片
//                    DownLoadUtils.download("http://image.bitautoimg.com/wap/car/3/" + brand_id_1 + ".png",
//                            brand_id_1 + ".png",
//                            path);
//                } catch (Exception e) {
//                    System.out.println(("文件下载异常" + brand_id_1 + ",png"));
//                    e.printStackTrace();
//                }

            }
            String jsonPath = path + brand_id_1 + ".json";
            if (!(new File(jsonPath)).exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(jsonPath)));
                printWriter.write(json);
                printWriter.close();
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).warn("write file error", e);
        }
    }
}
