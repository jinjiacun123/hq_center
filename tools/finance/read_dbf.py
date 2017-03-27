# -*- coding:utf-8 -*- 
import struct, datetime, decimal, itertools
import MySQLdb

def dbfreader(f):
    """Returns an iterator over records in a Xbase DBF file.

    The first row returned contains the field names.
    The second row contains field specs: (type, size, decimal places).
    Subsequent rows contain the data records.
    If a record is marked as deleted, it is skipped.

    File should be opened for binary reads.

    """
    # See DBF format spec at:
    #     http://www.pgts.com.au/download/public/xbase.htm#DBF_STRUCT

    numrec, lenheader = struct.unpack('<xxxxLH22x', f.read(32))    
    numfields = (lenheader - 33) // 32

    fields = []
    for fieldno in xrange(numfields):
        name, typ, size, deci = struct.unpack('<11sc4xBB14x', f.read(32))
        name = name.replace('\0', '')       # eliminate NULs from string   
        fields.append((name, typ, size, deci))
    yield [field[0] for field in fields]
    yield [tuple(field[1:]) for field in fields]

    terminator = f.read(1)
    assert terminator == '\r'

    fields.insert(0, ('DeletionFlag', 'C', 1, 0))
    fmt = ''.join(['%ds' % fieldinfo[2] for fieldinfo in fields])
    fmtsiz = struct.calcsize(fmt)
    for i in xrange(numrec):
        record = struct.unpack(fmt, f.read(fmtsiz))
        if record[0] != ' ':
            continue                        # deleted record
        result = []
        for (name, typ, size, deci), value in itertools.izip(fields, record):
            if name == 'DeletionFlag':
                continue
            if typ == "N":
                value = value.replace('\0', '').lstrip()
                if value == '':
                    value = 0
                elif deci:
                    value = decimal.Decimal(value)
                else:
                    value = int(value)
            elif typ == 'D':
                y, m, d = int(value[:4]), int(value[4:6]), int(value[6:8])
                value = datetime.date(y, m, d)
            elif typ == 'L':
                value = (value in 'YyTt' and 'T') or (value in 'NnFf' and 'F') or '?'
            elif typ == 'F':
                value = float(value)
            result.append(value)
        yield result


def dbfwriter(f, fieldnames, fieldspecs, records):
    """ Return a string suitable for writing directly to a binary dbf file.

    File f should be open for writing in a binary mode.

    Fieldnames should be no longer than ten characters and not include \x00.
    Fieldspecs are in the form (type, size, deci) where
        type is one of:
            C for ascii character data
            M for ascii character memo data (real memo fields not supported)
            D for datetime objects
            N for ints or decimal objects
            L for logical values 'T', 'F', or '?'
        size is the field width
        deci is the number of decimal places in the provided decimal object
    Records can be an iterable over the records (sequences of field values).
    
    """
    # header info
    ver = 3
    now = datetime.datetime.now()
    yr, mon, day = now.year-1900, now.month, now.day
    numrec = len(records)
    numfields = len(fieldspecs)
    lenheader = numfields * 32 + 33
    lenrecord = sum(field[1] for field in fieldspecs) + 1
    hdr = struct.pack('<BBBBLHH20x', ver, yr, mon, day, numrec, lenheader, lenrecord)
    f.write(hdr)
                      
    # field specs
    for name, (typ, size, deci) in itertools.izip(fieldnames, fieldspecs):
        name = name.ljust(11, '\x00')
        fld = struct.pack('<11sc4xBB14x', name, typ, size, deci)
        f.write(fld)

    # terminator
    f.write('\r')

    # records
    for record in records:
        f.write(' ')                        # deletion flag
        for (typ, size, deci), value in itertools.izip(fieldspecs, record):
            if typ == "N":
                value = str(value).rjust(size, ' ')
            elif typ == 'D':
                value = value.strftime('%Y%m%d')
            elif typ == 'L':
                value = str(value)[0].upper()
            else:
                value = str(value)[:size].ljust(size, ' ')
            assert len(value) == size
            f.write(value)

    # End of file
    f.write('\x1A')

def write_to_finance(item):
	try:
	    conn=MySQLdb.connect(host='192.168.1.233',user='root',passwd='root',db='quote_gp',port=3306)
	    cur=conn.cursor()
	    sqli='''insert into `finance`(symbol,     GXRQ,   ZGB,      GJG,    FQRFRG,
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
	    cur.execute(sqli,item)
	    #cur.execute("insert into `ritht`(symbol,publish_date,giving_stock,placing_Stock,giving_price,bonus) values()")
	    cur.close()
	    conn.close()
	except MySQLdb.Error,e:
	     print "Mysql Error %d: %s" % (e.args[0], e.args[1])

# -------------------------------------------------------
# Example calls
if __name__ == '__main__':
    import sys, csv
    from cStringIO import StringIO
    from operator import itemgetter

    # Read a database
    filename = 'F:/tools/hq_center/tools/finance/basic.DBF'      
    if len(sys.argv) == 2:
        filename = sys.argv[1]
    f = open(filename, 'rb')
    db = list(dbfreader(f))
    f.close()
    i = 0
    for record in db:
#        print record
        i += 1
        if i>2:
            market = record[0]
            gpdm = record[1]
            symbol  = ''
            print "market:%s" % (market)
            
            if int(market) == 0:
                symbol = 'SZ' + gpdm
            elif int(market) == 1:
                symbol = 'SH' + gpdm
            print "symbol:%s" % (symbol)
            #print record
            write_to_finance((symbol,record[2],record[3],record[4],record[5],
            record[6],record[7],record[8],record[9],record[10],record[11],
            record[12],record[13],record[14],record[15],record[16],record[17],
            record[18],record[19],record[20],record[21],record[22],record[23],
            record[24],record[25],record[26],record[27],record[28],record[29],
            record[30],record[31],record[32],record[33],record[34],record[35],
            record[36],record[37],record[38],record[39]))


    '''
    fieldnames, fieldspecs, records = db[0], db[1], db[2:]

    # Alter the database
    del records[4]
    records.sort(key=itemgetter(4))

    # Remove a field
    del fieldnames[0]
    del fieldspecs[0]
    records = [rec[1:] for rec in records]

    # Create a new DBF
    f = StringIO()
    dbfwriter(f, fieldnames, fieldspecs, records)

    # Read the data back from the new DBF
    print '-' * 20    
    f.seek(0)
    for line in dbfreader(f):
        print line
    f.close()

    # Convert to CSV
    print '.' * 20    
    f = StringIO()
    csv.writer(f).writerow(fieldnames)    
    csv.writer(f).writerows(records)
    print f.getvalue()
    f.close()
    '''
