/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.bean.quote.finance.FinanceData;
import com.otod.db.Connector;
import com.otod.util.ApplicationConstant;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinanceDao extends ParentDao {

    public List<FinanceData> getList() {
        List<FinanceData> list = new ArrayList<FinanceData>();
        String sql = "select * from finance";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);            
            rs = ps.executeQuery();
            while (rs.next()) {
                FinanceData financeData = new FinanceData();
                financeData.setDataType(ApplicationConstant.DB_DATA);
                financeData.setId(rs.getInt("id"));
                financeData.setSymbol(rs.getString("symbol"));
                financeData.setGxrq(rs.getInt("gxrq"));        //�����������
		financeData.setZgb(rs.getInt("zgb"));         //�ܹɱ�',
		financeData.setGjg(rs.getInt("gjg"));         //'���ҹ�',
		financeData.setFqrfrg(rs.getInt("fqrfrg"));      //'�����˹�',
		financeData.setFrg(rs.getInt("frg"));         //'���˹�',
		financeData.setBg(rs.getInt("bg"));          //'B��',
		financeData.setHg(rs.getInt("hg"));          //'H��',
		financeData.setLtag(rs.getInt("ltag"));        //'��ͨA��',
		financeData.setZgg(rs.getInt("zgg"));         //'ְ����',
		financeData.setZpg(rs.getInt("zpg"));         //'ת���',
		financeData.setZzc(rs.getInt("zzc"));         //'���ʲ�',
		financeData.setLdzc(rs.getInt("ldzc"));        //'�����ʲ�',
		financeData.setGdzc(rs.getInt("gdzc"));        //'�̶��ʲ�',
		financeData.setWxzc(rs.getInt("wxzc"));        //'�����ʲ�',
		financeData.setCqtz(rs.getInt("cqtz"));        //'�ɶ�����',
		financeData.setLdfz(rs.getDouble("ldfz"));        //'������ծ',
		financeData.setCqfz(rs.getInt("cqfz"));        //'������Ȩ',
		financeData.setZbgjj(rs.getInt("zbgjj"));       //'������',
		financeData.setJzc(rs.getInt("jzc"));         //'���ʲ�            JZC/ZGB=ÿ�ɾ��ʲ�',
		financeData.setZysy(rs.getInt("zysy"));        //'Ӫҵ����',
		financeData.setZyly(rs.getInt("zyly"));        //'Ӫҵ�ɱ�',
		financeData.setQtly(rs.getInt("qtly"));        //'Ӧ���˿�',
		financeData.setYyly(rs.getInt("yyly"));        //'Ӫҵ����',
		financeData.setTzsy(rs.getInt("tzsy"));        //'Ͷ������',
		financeData.setBtsy(rs.getInt("btsy"));        //'��Ӫ�ֽ���',
		financeData.setYywsz(rs.getInt("yywsz"));       //'���ֽ���',
		financeData.setSnsytz(rs.getInt("snsytz"));      //'���',
		financeData.setLyze(rs.getInt("lyze"));        //'�����ܶ�',
		financeData.setShly(rs.getDouble("shly"));        //'˰������',
		financeData.setJly(rs.getInt("jly"));         //'������             JLY/ZGB=ÿ������',
		financeData.setWfply(rs.getInt("wfply"));       //'δ��������',
		financeData.setTzmgjz(rs.getInt("tzmgjz"));      //'�������ʲ�',
		financeData.setDy(rs.getInt("dy"));          //'����',
		financeData.setHy(rs.getInt("hy"));          //'��ҵ',
		financeData.setZbnb(rs.getInt("zbnb"));        //'�����·� 9 ���������� 12�����걨',
		financeData.setSsdate(rs.getString("ssdate"));      //'��������',
		financeData.setModidate(rs.getString("modidate"));    //'��',
		financeData.setGdrs(rs.getString("gdrs"));        //'��',
                list.add(0, financeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }
    
    public FinanceData getBySymbol(String symbol)
    {
        FinanceData financeData = new FinanceData();
        String sql = "select * from finance where symbol=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);  
            ps.setString(1, symbol);
            rs = ps.executeQuery();
            while (rs.next()) {
                financeData.setDataType(ApplicationConstant.DB_DATA);
                financeData.setId(rs.getInt("id"));
                financeData.setSymbol(rs.getString("symbol"));
                financeData.setGxrq(rs.getInt("gxrq"));        //�����������
		financeData.setZgb(rs.getInt("zgb"));         //�ܹɱ�',
		financeData.setGjg(rs.getInt("gjg"));         //'���ҹ�',
		financeData.setFqrfrg(rs.getInt("fqrfrg"));      //'�����˹�',
		financeData.setFrg(rs.getInt("frg"));         //'���˹�',
		financeData.setBg(rs.getInt("bg"));          //'B��',
		financeData.setHg(rs.getInt("hg"));          //'H��',
		financeData.setLtag(rs.getInt("ltag"));        //'��ͨA��',
		financeData.setZgg(rs.getInt("zgg"));         //'ְ����',
		financeData.setZpg(rs.getInt("zpg"));         //'ת���',
		financeData.setZzc(rs.getDouble("zzc"));         //'���ʲ�',
		financeData.setLdzc(rs.getInt("ldzc"));        //'�����ʲ�',
		financeData.setGdzc(rs.getInt("gdzc"));        //'�̶��ʲ�',
		financeData.setWxzc(rs.getInt("wxzc"));        //'�����ʲ�',
		financeData.setCqtz(rs.getInt("cqtz"));        //'�ɶ�����',
		financeData.setLdfz(rs.getDouble("ldfz"));        //'������ծ',
		financeData.setCqfz(rs.getInt("cqfz"));        //'������Ȩ',
		financeData.setZbgjj(rs.getInt("zbgjj"));       //'������',
		financeData.setJzc(rs.getInt("jzc"));         //'���ʲ�            JZC/ZGB=ÿ�ɾ��ʲ�',
		financeData.setZysy(rs.getInt("zysy"));        //'Ӫҵ����',
		financeData.setZyly(rs.getInt("zyly"));        //'Ӫҵ�ɱ�',
		financeData.setQtly(rs.getInt("qtly"));        //'Ӧ���˿�',
		financeData.setYyly(rs.getInt("yyly"));        //'Ӫҵ����',
		financeData.setTzsy(rs.getInt("tzsy"));        //'Ͷ������',
		financeData.setBtsy(rs.getInt("btsy"));        //'��Ӫ�ֽ���',
		financeData.setYywsz(rs.getInt("yywsz"));       //'���ֽ���',
		financeData.setSnsytz(rs.getInt("snsytz"));      //'���',
		financeData.setLyze(rs.getInt("lyze"));        //'�����ܶ�',
		financeData.setShly(rs.getDouble("shly"));        //'˰������',
		financeData.setJly(rs.getInt("jly"));         //'������             JLY/ZGB=ÿ������',
		financeData.setWfply(rs.getInt("wfply"));       //'δ��������',
		financeData.setTzmgjz(rs.getInt("tzmgjz"));      //'�������ʲ�',
		financeData.setDy(rs.getInt("dy"));          //'����',
		financeData.setHy(rs.getInt("hy"));          //'��ҵ',
		financeData.setZbnb(rs.getInt("zbnb"));        //'�����·� 9 ���������� 12�����걨',
//		financeData.setSsdate(rs.getString("ssdate"));      //'��������',
		financeData.setModidate(rs.getString("modidate"));    //'��',
		financeData.setGdrs(rs.getString("gdrs"));        //'��',
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return financeData;
    }
}
