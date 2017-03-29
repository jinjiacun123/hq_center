/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.finance;

import com.otod.util.ApplicationConstant;

/**
 * 财务数据信息类
 *
 * @author Administrator
 */
public class FinanceData {

    private int    id;
    private String symbol;      //证券代码
    private int    gxrq;        //财务更新日期
    private int    zgb;         //总股本',
    private int    gjg;         //'国家股',
    private double fqrfrg;      //'发起人股',
    private double frg;         //'法人股',
    private int    bg;          //'B股',
    private int    hg;          //'H股',
    private double ltag;        //'流通A股',
    private double zgg;         //'职工股',
    private int    zpg;         //'转配股',
    private double zzc;         //'总资产',
    private double ldzc;        //'流动资产',
    private double gdzc;        //'固定资产',
    private double wxzc;        //'无形资产',
    private int    cqtz;        //'股东人数',
    private double ldfz;        //'流动负债',
    private double cqfz;        //'少数股权',
    private double zbgjj;       //'公积金',
    private double jzc;         //'净资产            JZC/ZGB=每股净资产',
    private double zysy;        //'营业收入',
    private double zyly;        //'营业成本',
    private double qtly;        //'应收账款',
    private double yyly;        //'营业利润',
    private double tzsy;        //'投资收益',
    private double btsy;        //'经营现金流',
    private double yywsz;       //'总现金流',
    private double snsytz;      //'存货',
    private double lyze;        //'利润总额',
    private double shly;        //'税后利润',
    private double jly;         //'净利润             JLY/ZGB=每股收益',
    private double wfply;       //'未分配利润',
    private double tzmgjz;      //'调整后净资产',
    private int    dy;          //'地域',
    private int    hy;          //'行业',
    private int    zbnb;        //'资料月份 9 代表三季报 12代表年报',
    private String ssdate;      //'上市日期',
    private String modidate;    //'空',
    private String gdrs;        //'空',
    private int dataType = ApplicationConstant.MEMORY_DATA;//数据类型，分为内存数据和数据库数据，默认为内存数据
    private int dbOperType;//数据库处理操作，有插入和更新两种

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getGxrq(){
        return gxrq;
    }

    public void setGxrq(int gxrq){
        this.gxrq = gxrq;
    }

    public int  getZgb(){
        return zgb;
    }

    public void setZgb(int zgb){
        this.zgb = zgb;
    }

    public int getGjg(){
        return gjg;
    }

    public void setGjg(int gjg){
        this.gjg = gjg;
    }

    public double getFqrfrg(){
         return fqrfrg;
    }

    public void setFqrfrg(int fqrfrg){
        this.fqrfrg = fqrfrg;
    }

    public double getFrg(){
        return frg;
    }

    public void setFrg(double frg){
        this.frg = frg;
    }

    public int getBg(){
	return bg;
    }

    public void setBg(int bg){
	this.bg = bg;
    }

    public int getHg(){
	return hg;
    }

    public void setHg(int hg){
	this.hg = hg;
    }

    public double getLtag(){
	return ltag;
    }
    public void setLtag(double ltag){
	this.ltag = ltag;
    }

    public double getZgg(){
	return zgg;
    }

    public void setZgg(double zgg){
	this.zgg = zgg;
    }

    public int getZpg(){
	return zpg;
    }

    public void setZpg(int zpg){
	this.zpg = zpg;
    }

    public double getZzc(){
	return zzc;
    }

    public void setZzc(double zzc){
	this.zzc = zzc;
    }

    public double getLdzc(){        //'流动资产',
	return ldzc;
    }

    public void setLdzc(double ldzc){
	this.ldzc = ldzc;
    }

    public double getGdzc(){        //'固定资产',
	return gdzc;
    }

    public void setGdzc(double gdzc){
	this.gdzc = gdzc;
    }

    public double getWxzc(){        //'无形资产',
	return wxzc;
    }
    
    public void setWxzc(double wxzc){
	this.wxzc = wxzc;
    }

    public int getCqtz(){        //'股东人数',
	return cqtz;
    }

    public void setCqtz(int cqtz){
	this.cqtz = cqtz;
    }

    public double getLdfz(){        //'流动负债',
	return ldfz;
    }

    public void setLdfz(double ldfz){
	this.ldfz = ldfz;
    }

    public double getCqfz(){        //'少数股权',
	return cqfz;
    }

    public void setCqfz(double cqfz){
	this.cqfz = cqfz;
    }

    public double getZbgjj(){       //'公积金',
	return zbgjj;
    }

    public void setZbgjj(double zbgjj){
	this.zbgjj = zbgjj;
    }

    public double getJzc(){         //'净资产            JZC/ZGB=每股净资产',
	return jzc;
    }

    public void setJzc(double jzc){
	this.jzc = jzc;
    }

    public double getZysy(){        //'营业收入',
	return zysy;
    }

    public void setZysy(double zysy){
	this.zysy = zysy;
    }

    public double getZyly(){        //'营业成本',
	return zyly;
    }

    public void setZyly(double zyly){
	this.zyly = zyly;
    }

    public double getQtly(){        //'应收账款',
	return qtly;
    }

    public void setQtly(double qtly){
	this.qtly = qtly;
    }

    public double getYyly(){        //'营业利润',
	return yyly;
    }

    public void setYyly(double yyly){
	this.yyly = yyly;
    }

    public double getTzsy(){        //'投资收益',
	return tzsy;
    }

    public void setTzsy(double tzsy){
	this.tzsy = tzsy;
    }

    public double getBtsy(){        //'经营现金流',
	return btsy;
    }

    public void setBtsy(double btsy){
	this.btsy = btsy;
    }

    public double getYywsz(){       //'总现金流',
	return yywsz;
    }

    public void setYywsz(double yywsz){
	this.yywsz = yywsz;
    }

    public double getSnsytz(){      //'存货',
	return snsytz;
    }

    public void setSnsytz(double snsytz){
	this.snsytz = snsytz;
    }
    
    public double getLyze(){        //'利润总额',
	return lyze;
    }

    public void setLyze(double lyze){
	this.lyze = lyze;
    }

    public double getShly(){        //'税后利润',
	return shly;
    }

    public void setShly(double shly){
	this.shly = shly;
    }

    public double getJly(){         //'净利润             JLY/ZGB=每股收益',
	return jly;
    }

    public void setJly(double jly){
	this.jly = jly;
    }

    public double getWfply(){       //'未分配利润',
	return jly;
    }
    
    public void setWfply(double jly){
	this.jly = jly;
    }

    public double getTzmgjz(){      //'调整后净资产',
	return tzmgjz;
    }

    public void setTzmgjz(double tzmgjz){
	this.tzmgjz = tzmgjz;
    }

    public int getDy(){          //'地域',
	return dy;
    }

    public void setDy(int dy){
	this.dy = dy;
    }

    public int getHy(){          //'行业',
	return hy;
    }

    public void setHy(int hy){
	this.hy = hy;
    }

    public int getZbnb(){        //'资料月份 9 代表三季报 12代表年报',
	return zbnb;
    }

    public void setZbnb(int zbnb){
	this.zbnb = zbnb;
    }

    public String getSsdate(){      //'上市日期',
	return ssdate;
    }

    public void setSsdate(String ssdate){
	this.ssdate = ssdate;
    }

    public String getModidate(){    //'空',
	return modidate;
    }
    
    public void setModidate(String modidate){
	this.modidate = modidate;
    }

    public String getGdrs(){        //'空',
	return gdrs;
    }
    
    public void setGdrs(String gdrs){
	this.gdrs = gdrs;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDbOperType() {
        return dbOperType;
    }

    public void setDbOperType(int dbOperType) {
        this.dbOperType = dbOperType;
    }

    @Override
    public FinanceData clone() {
        FinanceData financeData = new FinanceData();
        financeData.id       = this.id;
	financeData.symbol   = this.symbol;
	financeData.gxrq     = this.gxrq;        //财务更新日期
        financeData.zgb      = this.zgb;         //总股本',
        financeData.gjg      = this.gjg;         //'国家股',
	financeData.fqrfrg   = this.fqrfrg;      //'发起人股',
	financeData.frg      = this.frg;         //'法人股',
        financeData.bg       = this.bg;          //'B股',
        financeData.hg      = this.hg;          //'H股',
	financeData.ltag     = this.ltag;        //'流通A股',
	financeData.zgg      = this.zgg;         //'职工股',
        financeData.zpg      = this.zpg;         //'转配股',
	financeData.zzc      = this.zzc;         //'总资产',
	financeData.ldzc     = this.ldzc;        //'流动资产',
	financeData.gdzc     = this.gdzc;        //'固定资产',
	financeData.wxzc     = this.wxzc;        //'无形资产',
        financeData.cqtz     = this.cqtz;        //'股东人数',
	financeData.ldfz     = this.ldfz;        //'流动负债',
	financeData.cqfz     = this.cqfz;        //'少数股权',
	financeData.zbgjj    = this.zbgjj;       //'公积金',
	financeData.jzc      = this.jzc;         //'净资产            JZC/ZGB=每股净资产',
	financeData.zysy     = this.zysy;        //'营业收入',
	financeData.zyly     = this.zyly;        //'营业成本',
	financeData.qtly     = this.qtly;        //'应收账款',
	financeData.yyly     = this.yyly;        //'营业利润',
	financeData.tzsy     = this.tzsy;        //'投资收益',
	financeData.btsy     = this.btsy;        //'经营现金流',
	financeData.yywsz    = this.yywsz;       //'总现金流',
	financeData.snsytz   = this.snsytz;      //'存货',
	financeData.lyze     = this.lyze;        //'利润总额',
	financeData.shly     = this.shly;        //'税后利润',
	financeData.jly      = this.jly;         //'净利润             JLY/ZGB=每股收益',
	financeData.wfply    = this.wfply;       //'未分配利润',
	financeData.tzmgjz   = this.tzmgjz;      //'调整后净资产',
        financeData.dy       = this.dy;          //'地域',
        financeData.hy       = this.hy;          //'行业',
        financeData.zbnb     = this.zbnb;        //'资料月份 9 代表三季报 12代表年报',
	financeData.ssdate   = this.ssdate;      //'上市日期',
	financeData.modidate = this.modidate;    //'空',
	financeData.gdrs     = this.gdrs;        //'空',
	financeData.dataType = this.dataType;
        financeData.dbOperType = this.dbOperType;
        return financeData;
    }
}
