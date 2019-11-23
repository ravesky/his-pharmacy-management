package com.pharmacy.management.result;

import com.pharmacy.management.bean.Drug;

import java.util.List;

/**
 * @author 徐奥飞
 * @date 2019-11-8 8:30
 */

public class DrugMagResult {

    // 响应码
    private int code;
    // 反馈信息
    private String message;
    // 反馈对象
    private List<Drug> drugs;
    // 反馈数据量
    private int num;

    public DrugMagResult(int code) {
        this.code = code;
    }

    public DrugMagResult(int code, String message, List<Drug> drugs, int num) {
        this.code = code;
        this.message = message;
        this.drugs = drugs;
        this.num = num;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
