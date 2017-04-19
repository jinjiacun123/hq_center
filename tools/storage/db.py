import MySQLdb
from help import read_ini

def write_to_finance(item):
    host   = read_ini('baseconf', 'host')
    user   = read_ini('baseconf', 'user')
    passwd = read_ini('baseconf', 'password')
    db     = read_ini('baseconf', 'db_name')
    port   = int(read_ini('baseconf', 'port'))
    try:
        #conn = MySQLdb.connect(host='192.168.1.233', user='root', passwd='root', db='quote_gp', port=3306)
        conn = MySQLdb.connect(host, user, passwd, db, port)
        cur = conn.cursor()
        sqli = '''replace into `finance`(symbol,     GXRQ,   ZGB,      GJG,    FQRFRG,
            FRG,        BG,     HG,       LTAG,   ZGG,
            ZPG,        ZZC,    LDZC,     GDZC,   WXZC,
            CQTZ,       LDFZ,   CQFZ,     ZBGJJ,  JZC,
            ZYSY,       ZYLY,   QTLY,     YYLY,   TZSY,
            BTSY,       YYWSZ,  SNSYTZ,   LYZE,   SHLY,
            JLY,        WFPLY,  TZMGJZ,   DY,     HY,
            ZBNB,       SSDATE, MODIDATE, GDRS) values(
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s,     %s,
            %s,         %s,     %s,       %s)'''
        cur.execute(sqli, item)
        # cur.execute("insert into `ritht`(symbol,publish_date,giving_stock,placing_Stock,giving_price,bonus) values()")
        cur.close()
        conn.close()
    except MySQLdb.Error, e:
        print "Mysql Error %d: %s" % (e.args[0], e.args[1])