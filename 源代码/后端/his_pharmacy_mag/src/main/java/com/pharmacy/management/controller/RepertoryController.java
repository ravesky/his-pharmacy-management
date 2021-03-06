package com.pharmacy.management.controller;

import com.pharmacy.management.bean.Drug;
import com.pharmacy.management.bean.Warehouse;
import com.pharmacy.management.dao.DrugDao;
import com.pharmacy.management.result.RepertoryResult;
import com.pharmacy.management.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class RepertoryController {

    @Autowired
    DrugService drugService;

    @Autowired
    DrugDao drugDao;

    // 解决跨域问题
    @CrossOrigin
    @PostMapping(value = "/api/query")
    @ResponseBody
    public RepertoryResult query(@RequestBody Drug requestDrug) {
        String mnemonicCode = requestDrug.getMnemonicCode();
        List<Drug> drugs = new ArrayList<>();
        drugs = drugService.getByMnemonicCodeLike("%"+ mnemonicCode +"%");
        if (drugs.size() == 0) {
            return new RepertoryResult(400, "没有匹配数据", null);
        } else {
            return new RepertoryResult(200, "操作执行成功", drugs);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/queryAll")
    @ResponseBody
    public RepertoryResult queryAll() {
        List<Drug> drugs = new ArrayList<>();
        drugs = drugService.getAll();
        return new RepertoryResult(200, "操作执行成功", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/deleteRepertory")
    @ResponseBody
    public RepertoryResult deleteRepertory(@RequestBody Drug requestDrug) {
        int id = requestDrug.getId();
        String mnemonicCode = requestDrug.getMnemonicCode();
        drugService.deleteRepertory(id);
        List<Drug>  drugs = new ArrayList<>();
        drugs = getDrugs(mnemonicCode);
        return new RepertoryResult(200, "删除完成", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/updateRepertory")
    @ResponseBody
    @Transactional
    public RepertoryResult updateRepertory(@RequestBody Drug requestDrug) {
        int id = requestDrug.getId();
        double drugsPrice = requestDrug.getDrugsPrice();
        int totalNum = requestDrug.getTotalNum();
        String mnemonicCode = requestDrug.getMnemonicCode();
        Drug drug = drugService.getById(id);
        drug.setDrugsPrice(drugsPrice);
        drug.setTotalNum(totalNum);
        List<Warehouse> warehouses = drug.getWarehouses();
        List<Warehouse> warehouses1 = requestDrug.getWarehouses();
        if (warehouses.get(0).getWarehouse() == warehouses1.get(0).getWarehouse()) {
            warehouses.get(0).setNum(warehouses1.get(0).getNum());
            warehouses.get(1).setWarehouse(warehouses1.get(1).getWarehouse());
            warehouses.get(1).setNum(warehouses1.get(1).getNum());
        } else {
            warehouses.get(0).setWarehouse(warehouses1.get(1).getWarehouse());
            warehouses.get(0).setNum(warehouses1.get(1).getNum());
            warehouses.get(1).setWarehouse(warehouses1.get(0).getWarehouse());
            warehouses.get(1).setNum(warehouses1.get(0).getNum());
        }
        drugDao.save(drug);
        List<Drug> drugs = getDrugs(mnemonicCode);
        return new RepertoryResult(200, "修改完成", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/updateWarehouse")
    @ResponseBody
    @Transactional
    public RepertoryResult updateWarehouse(@RequestBody Map drugsOptions) {
        List<Integer> ids = new ArrayList<>();
        ids = (List<Integer>) drugsOptions.get("ids");
        String mnemonicCode = (String) drugsOptions.get("mnemonicCode");
        String warehouse = (String) drugsOptions.get("warehouse");
        System.out.println(ids);
        System.out.println(mnemonicCode);
        System.out.println(warehouse);
        for (int id : ids) {
            Drug drug = new Drug();
            drug = drugService.getById(id);
            if (drug.getWarehouses().get(0).equals(warehouse)) {
                drug.getWarehouses().get(0).setNum(drug.getWarehouses().get(0).getNum() + drug.getWarehouses().get(1).getNum());
                drug.getWarehouses().get(1).setNum(0);
            } else {
                drug.getWarehouses().get(1).setNum(drug.getWarehouses().get(0).getNum() + drug.getWarehouses().get(1).getNum());
                drug.getWarehouses().get(0).setNum(0);
            }
            drugDao.save(drug);
        }
        List<Drug> drugs = new ArrayList<>();
        drugs = getDrugs(mnemonicCode);
        return new RepertoryResult(200, "批量修改完成", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/addRepertory")
    @ResponseBody
    @Transactional
    public RepertoryResult addRepertory(@RequestBody Drug requestDrug) {
        drugDao.save(requestDrug);
        List<Drug> drugs = new ArrayList<>();
        drugs = drugService.getAll();
        return new RepertoryResult(200, "添加药品成功", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/deleteOptions")
    @ResponseBody
    public RepertoryResult deleteOptions(@RequestBody Map drugsOptions) {
        List<Integer> ids = new ArrayList<>();
        ids = (List<Integer>) drugsOptions.get("ids");
        String mnemonicCode = (String) drugsOptions.get("mnemonicCode");
        for (int id : ids) {
            drugService.deleteRepertory(id);
        }
        List<Drug> drugs = new ArrayList<>();
        drugs = getDrugs(mnemonicCode);
        return new RepertoryResult(200, "批量删除完成", drugs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/importDrugs")
    @ResponseBody
    public List<Drug> importDrugs(@RequestBody Map map) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(map.get("drugs"));
        List<Map> list = new ArrayList<>();
        List<Drug> drugs = new ArrayList<>();
        Drug drug = new Drug();
        list = (List<Map>) map.get("drugs");
        for (Map m : list) {
            drug = new Drug();
            drug.setDrugsCode(m.get("drugsCode").toString());
            drug.setDrugsName(m.get("drugsName").toString());
            drug.setDrugsFormat(m.get("drugsFormat").toString());
            drug.setDrugsUnit(m.get("drugsUnit").toString());
            drug.setManufacturer(m.get("manufacturer").toString());
            drug.setDrugsDosageID((Integer) m.get("drugsDosageID"));
            drug.setDrugsTypeID((Integer) m.get("drugsTypeID"));
            drug.setDrugsPrice(Double.valueOf(m.get("drugsPrice").toString()));
            drug.setMnemonicCode(m.get("mnemonicCode").toString());
            drug.setCreationDate(format.parse(m.get("creationDate").toString()));
            drug.setTotalNum((Integer) m.get("totalNum"));
            drug.setSaveRequire(m.get("saveRequire").toString());
            List<Warehouse> warehouses = drug.getWarehouses();
            Warehouse warehouse = new Warehouse();
            warehouse.setWarehouse("储藏室");
            warehouse.setNum(drug.getTotalNum());
            warehouse.setDrug(drug);
            warehouses.add(warehouse);
            drugDao.save(drug);
            drugs.add(drug);
        }
        return drugs;
    }

    private List<Drug> getDrugs(String mnemonicCode) {
        List<Drug> drugs = new ArrayList<>();
        if (mnemonicCode.isEmpty()) {
            drugs = drugService.getAll();
        } else {
            drugs = drugService.getByMnemonicCodeLike("%"+ mnemonicCode +"%");
        }
        return drugs;
    }
}
