package org.example.pageprocessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.SpiderManager;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class CarBrandPageProcessor implements PageProcessor {
    public static final String BRAND_LETTER = "brand name letter";
    public static final String BRAND_BRAND_ID_1 = "brand ID one level";
    public static final String BRAND_JSON = "one level brand json";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.setRawText(page.getRawText().substring(14, page.getRawText().length() - 1));
        page.putField(BRAND_JSON, page.getRawText());

        String url = page.getUrl().get();

        JSONObject jsonObject = JSON.parseObject(page.getRawText());
        JSONObject brand = jsonObject.getJSONObject("brand");
        if (SpiderManager.URL_START.equals(url)) {// 全部一级品牌 > 由此获取二级品牌 及具体品牌车型
            for (char letter = 'A'; letter <= 'Z'; letter++) {
                JSONArray jsonArray = brand.getJSONArray(String.valueOf(letter));
                if (jsonArray != null && jsonArray.size() != 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String brandName = jsonArray.getJSONObject(i).getString("id");
                        page.addTargetRequest("http://api.car.bitauto.com/CarInfo/getlefttreejson.ashx?tagtype=chexing&pagetype=masterbrand&objid=" + brandName);
                    }
                }
            }
        } else { //二级品牌
            String carID_1 = url.substring(url.lastIndexOf("=") + 1);// 一级车型品牌ID

            LETTER:
            for (char letter = 'A'; letter <= 'Z'; letter++) {
                String letterKey = String.valueOf(letter);//一级车型品牌名字匹配首字母
                JSONArray jsonArray = brand.getJSONArray(letterKey);
                if (jsonArray != null && jsonArray.size() != 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject brand1JsonObject = jsonArray.getJSONObject(i);
                        String brandID = brand1JsonObject.getString("id");//一级车型品牌ID
                        if (brandID.equals(carID_1)) {
                            page.putField(BRAND_LETTER, letterKey);
                            page.putField(BRAND_BRAND_ID_1, brandID);

                            JSONArray brand2JsonArray = brand1JsonObject.getJSONArray("child");
                            for (int j = 0; j < brand2JsonArray.size(); j++) {
                                JSONObject brand2JsonObject = brand2JsonArray.getJSONObject(j);
                                String brand2url = brand2JsonObject.getString("url");
                                String brand2ID = brand2url.substring(brand2url.lastIndexOf("_") + 1, brand2url.length() - 1);
                                JSONArray brand3JsonArray = brand2JsonObject.getJSONArray("child");
                                if (brand3JsonArray != null && brand3JsonArray.size() != 0) {

                                    for (int m = 0; m < brand3JsonArray.size(); m++) {
                                        JSONObject brand3JsonObject = brand3JsonArray.getJSONObject(m);
//                                    String csID = brand3JsonObject.getJSONObject("activity").getString("CsId");
                                        String brand3Url = brand3JsonObject.getString("url");
                                        String brand3ID = brand3Url.substring(brand3Url.lastIndexOf("_") + 1, brand3Url.length() - 1);

                                        SpiderManager.getCarTypes(brand3ID, letterKey, brandID, brand2ID);
                                    }
                                }
                            }
                            break LETTER;
                        }
                    }
                }
            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
