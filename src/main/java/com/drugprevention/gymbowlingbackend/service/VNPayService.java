package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.config.VNPayConfig;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    private final VNPayConfig vnPayConfig;

    public VNPayService(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    public String createPaymentUrl(long amount, String orderInfo, String orderId) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VNPayConfig.VERSION);
        vnpParams.put("vnp_Command", VNPayConfig.COMMAND);
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay uses cents
        vnpParams.put("vnp_CurrCode", VNPayConfig.CURRENCY_CODE);
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", VNPayConfig.ORDER_TYPE);
        vnpParams.put("vnp_Locale", VNPayConfig.LOCALE);
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // Build query string
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                try {
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnpSecureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);

        return vnPayConfig.getPayUrl() + "?" + query.toString();
    }

    public boolean validateSignature(Map<String, String> params) {
        String vnpSecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String calculatedHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        return calculatedHash.equals(vnpSecureHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
