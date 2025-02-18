package com.zhoujh.lvtu.utils;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class HuanXinUtils {
    public static final String TAG = "HuanXinUtils";
    /**
     * 构建环信ID
     * 去除其中的“-”，并每四位字符为一组进行组内逆序
     *
     * @param uuid 用户的 UUID
     * @return 构建得到的 环信ID
     */
    public static String createHXId(String uuid) {
        // 去除字符串中的 -
        String uuidWithoutDash = uuid.replace("-", "");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < uuidWithoutDash.length(); i += 4) {
            String group = uuidWithoutDash.substring(i, Math.min(i + 4, uuidWithoutDash.length()));
            // 对每一组进行逆序
            StringBuilder reversedGroup = new StringBuilder(group).reverse();
            result.append(reversedGroup);
        }
        return result.toString();
    }

    /**
     * 还原 UUID
     *
     * @param processedId
     * @returnm 还原得到的 UUID
     */
    public static String restoreUUID(String processedId) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < processedId.length(); i += 4) {
            String group = processedId.substring(i, Math.min(i + 4, processedId.length()));
            // 对每一组进行逆序
            StringBuilder reversedGroup = new StringBuilder(group).reverse();
            result.append(reversedGroup);
        }
        // 在指定位置插入 -
        StringBuilder finalUUID = new StringBuilder(result);
        finalUUID.insert(8, '-');
        finalUUID.insert(13, '-');
        finalUUID.insert(18, '-');
        finalUUID.insert(23, '-');
        return finalUUID.toString();
    }
}
