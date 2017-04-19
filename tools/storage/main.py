# -*- coding:utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import sys, csv
from cStringIO import StringIO
from operator import itemgetter
from dbf import dbfreader
from db import write_to_finance
from help import http_down
from help import read_ini

def do_finance():
    # read config
    finance_url       = read_ini("urlconf","finance_url")
    finance_data_dir  = read_ini('dataconf', 'data_dir')
    finance_file_name = read_ini('dataconf',"finance_file_name")

    #down file
    if not http_down(finance_url, finance_data_dir+finance_file_name):
        print 'down fail'
        exit(-1)

    print 'down success'
    file_name = finance_data_dir + finance_file_name
    # Read a database
    if len(sys.argv) == 2:
        file_name = sys.argv[1]
    f = open(file_name, 'rb')
    db = list(dbfreader(f))
    f.close()
    i = 0
    for record in db:
        #        print record
        i += 1
        if i > 2:
            market = record[0]
            gpdm = record[1]
            symbol = ''
            print "market:%s" % (market)

            if int(market) == 0:
                symbol = 'SZ' + gpdm
            elif int(market) == 1:
                symbol = 'SH' + gpdm
            print "symbol:%s" % (symbol)
            # print record
            write_to_finance((symbol, record[2], record[3], record[4], record[5],
                              record[6], record[7], record[8], record[9], record[10], record[11],
                              record[12], record[13], record[14], record[15], record[16], record[17],
                              record[18], record[19], record[20], record[21], record[22], record[23],
                              record[24], record[25], record[26], record[27], record[28], record[29],
                              record[30], record[31], record[32], record[33], record[34], record[35],
                              record[36], record[37], record[38], record[39]))
            # deal finance

def do_right():
    pass

def main():
    do_finance()


if __name__ == "__main__":
    main()