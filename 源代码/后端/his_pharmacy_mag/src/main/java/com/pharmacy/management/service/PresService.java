package com.pharmacy.management.service;


import com.pharmacy.management.bean.Prescription;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PresService {
    public List<Prescription> getByPresCode(int PresCode);
    public List<Prescription> getAll();
    public List<Prescription> getSent();
    public List<Prescription> getNotSent();
    public Prescription getByPresId(int pres_id);
    public void Update(Prescription prescription);
    public void DeleteById(int id);
}
