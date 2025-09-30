package com.plh.parking.service.Impl;

import com.plh.parking.service.ReportSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportSeviceImpl implements ReportSevice {

    @Override
    public byte[] residentPayments() {
        return new byte[0];
    }

}
