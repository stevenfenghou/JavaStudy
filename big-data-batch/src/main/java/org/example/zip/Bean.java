package org.example.zip;

import org.example.utils.RandomDatagenerater;

import java.io.Serializable;
import java.util.*;

public class Bean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long idno = 0;
    private long id;
    private String name;
    private int age;
    private int total;
    private int provinceCode;
    private String provinceName;
    private int terminalNo;
    private String terminalName;
    private List<String> betInfos;
    private Map<String, String> details;

    public Bean() {
    }

    public static Bean createWithRadom() {
        Bean bean = new Bean();
        bean.setId(idno++);
        bean.setName(RandomDatagenerater.randomChineseCharString(2,3));
        bean.setAge(RandomDatagenerater.randomInt(90));
        bean.setProvinceCode(RandomDatagenerater.randomInt(35));
        bean.setProvinceName(RandomDatagenerater.randomChineseCharString(2,4));
        bean.setTerminalNo(RandomDatagenerater.randomInt(200000));
        bean.setTerminalName(RandomDatagenerater.randomChineseCharString(6,10));
        List<String> abetInfos = new ArrayList<>();
        for (int i = 0; i < 5 + RandomDatagenerater.randomInt(5); i++) {
            abetInfos.add(RandomDatagenerater.randomChineseCharString(10,20));
        }
        bean.setBetInfos(abetInfos);
        Map<String, String> adetails = new HashMap<>();
        for (int i = 0; i < 3 + RandomDatagenerater.randomInt(5); i++) {
            adetails.put(RandomDatagenerater.randomAsciiString(10,20), RandomDatagenerater.randomChineseCharString(10,20));
        }
        bean.setDetails(adetails);
        return bean;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bean.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("age=" + age)
                .add("total=" + total)
                .add("provinceCode=" + provinceCode)
                .add("provinceName='" + provinceName + "'")
                .add("terminalNo=" + terminalNo)
                .add("terminalName='" + terminalName + "'")
                .add("betInfos=" + betInfos)
                .add("details=" + details)
                .toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(int terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public List<String> getBetInfos() {
        return betInfos;
    }

    public void setBetInfos(List<String> betInfos) {
        this.betInfos = betInfos;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
