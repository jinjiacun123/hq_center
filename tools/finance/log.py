import logging  
import logging.handlers  
  
LOG_FILE = 'tst.log'  
  
handler = logging.handlers.RotatingFileHandler(LOG_FILE, maxBytes = 1024*1024, backupCount = 5) # ʵ����handler   
fmt = '%(asctime)s - %(filename)s:%(lineno)s - %(name)s - %(message)s'  
  
formatter = logging.Formatter(fmt)   # ʵ����formatter  
handler.setFormatter(formatter)      # Ϊhandler���formatter  
  
logger = logging.getLogger('tst')    # ��ȡ��Ϊtst��logger  
logger.addHandler(handler)           # Ϊlogger���handler  
logger.setLevel(logging.DEBUG)  
  
logger.info('first info message')  
logger.debug('first debug message')  