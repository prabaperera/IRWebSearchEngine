
def read_file(filename_rd):     
    global current_time
    global list_childdict
    global list_key
    global list_procchilddict
    global list_prockey
    global dict_key
    global proc_key
    global set_key
    
    with open(filename_rd) as f:
        for line in f:
            process_line(line)
            #if check_time(line) is True:
                #process_proc_line(line)
            #else:
                #write_proc_file()
                #print('writing..')
                #current_time=current_time+1
                #dict_key.clear()
                #list_childdict.clear()
                #list_key.clear()
                #proc_key.clear()
                #list_procchilddict.clear()
                #list_prockey.clear()
                #process_proc_line(line)
        key_list=list(set_key)
        for item in key_list:
            write_auth_file(item,0)
            
                
                

def check_time(line):
    global current_time
    
    time,u_domain,com,prc,st_end=line.split(",")
    print('time: '+str(time))
    print('current_time: '+ str(current_time))
    
    
    if time == current_time:
        print('here1..')
        return True
    else:
        print('here2..')
        return False
    
    
def write_auth_file(key,time):
    global list_key
    global list_net_type
    global list_auth_type
    global dict_key
    
    #list_auth_type=list(set_auth_type)
    #list_net_type=list(set_net_type)
    
    #for key in list_key:
       # record=""
       # id=list_key.index(key)
       # record=record +','+ str(id)
    
    ind=dict_key.get(key,-1)
    
    record=key
    if ind != -1:
        child_dict=list_childdict[ind]
        record=record+','+str(child_dict.get('start_time',0))+','+ str (time)
        record=record+','+ str(child_dict.get('dest_u_dom_cnt',0))
        record=record+','+ str(child_dict.get('dest_com_cnt',0))
      
        un_dest=child_dict['un_dest']
        arr_un_dest=un_dest.split(',')
        set_un_dest=set(arr_un_dest)
        record=record+','+ str(len(set_un_dest))
        
                  
        un_dest_dom=child_dict['un_dest_dom']
        arr_un_dest_dom=un_dest_dom.split(',')
        set_un_dest_do = set(arr_un_dest_dom) 
        record=record+','+ str(len(set_un_dest_do))
                     
        un_dest_ratio=int(child_dict.get('dest_com_cnt',0))/len(set_un_dest)
        un_dest_ratio = format(un_dest_ratio, '.4f')
        un_dest_dom_ratio=int(child_dict.get('dest_u_dom_cnt',0))/len(set_un_dest_do)
        un_dest_dom_ratio = format(un_dest_dom_ratio, '.4f')
        record=record+','+str(un_dest_ratio)
        record=record+','+str(un_dest_dom_ratio)                            

                      
        for auth_type in list_auth_type:
            auth_type='auth_'+auth_type
            record=record+','+ str(child_dict.get(auth_type,0))
        for net_type in list_net_type :
            net_type='log_'+net_type
            record=record+','+ str(child_dict.get(net_type,0))
        for auth_ori in list_auth_ori:
            record=record+','+ str(child_dict.get(auth_ori,0))
        for suc_fail in list_suc_fail:
            record=record+','+ str(child_dict.get(suc_fail,0))
        
        time_dif=int(time)-int(child_dict.get('start_time',0))
        if time_dif != 0:
            time_dif=1/time_dif
            time_dif=format(time_dif, '.4f')
        record=record+','+ str(time_dif)
        #print(record)
        file_wr_auth.write(record)
        file_wr_auth.write('\n') 



def write_proc_file():
    global list_prockey
    global list_procchilddict
    global set_proc
    global proc_key
    global list_st_end
    
    list_proc_name=list(set_proc)
    
    
    for key in list_prockey:
        record=""
        id=list_prockey.index(key)
        record=record +','+ key+','+str(id)
        ind=proc_key.get(key,-1)
        if ind != -1:
            child_dict=list_procchilddict[ind]
            record=record+','+ str(child_dict.get('prc_cnt',0))
            #for p_name in list_proc_name:
                #record=record+','+str(child_dict.get(p_name,0))
            for st_end in list_st_end:
                #print(st_end +' : '+str(child_dict.get(st_end,0)))
                record=record+','+str(child_dict.get(st_end,0))
        file_wr_proc.write(record)
        file_wr_proc.write('\n') 
        
        


def process_proc_line(line):
	#time,user@domain,computer,process name,start/end =line.split(",")
    time,u_domain,com,prc,st_end=line.split(",")
    
    
    st_end=st_end.strip()   
    
    global proc_key
    global list_procchilddict
    global list_prockey
    global set_proc
	
    u_domain=u_domain.split("$")[0]
    key=time+'~'+u_domain+'~'+com
    ind=proc_key.get(key,-1)
	
    if ind !=-1:
        child_dict=list_procchilddict[ind]
        prc_cnt=child_dict.get('prc_cnt',0)
        prc_cnt=prc_cnt+1
        child_dict['prc_cnt']=prc_cnt
		
        #p_prc_cnt=child_dict.get(prc,0)
        #prc_cnt=p_prc_cnt+1
        #child_dict[prc]=p_prc_cnt
                  
        st_end_cnt=child_dict.get(st_end,0)
        st_end_cnt=st_end_cnt+1
        child_dict[st_end]=st_end_cnt
	
    else:
        child_dict={}
        child_dict['prc_cnt']=1
        #child_dict[prc]=1
        child_dict[st_end]=1
        list_procchilddict.append(child_dict)
        new_indx=len(list_procchilddict)-1
        proc_key[key]=new_indx
        list_prockey.append(key)
        set_proc.add(prc)
            #print('st_end: '+st_end+str(child_dict[st_end]))



	
def process_line(line):
    time,src_u_domain,dest_u_domain,src_com,dest_com,auth_type,logon_type,auth_ori,suc_fail=line.split(",")
    suc_fail=suc_fail.strip()
    
    global line_no
    #print(str(line_no)+' : '+line)
    line_no=line_no+1
    #same_come to same
    #identify_login_logoff()
    #list_auth_ori=['LogOn','LogOff']
    #list_suc_fail=['Success','Fail']
    
    global dict_key
    global list_childdict
    global list_key
    global set_auth_type
    global set_net_type
    global set_key
    global list_auth_ori
    global list_suc_fail
    

    
    if (src_com==dest_com) and (auth_ori == list_auth_ori[0]) and (suc_fail == list_suc_fail[0]):
        action=list_auth_ori[0]
    elif(src_com==dest_com) and (auth_ori == list_auth_ori[1]) and (suc_fail == list_suc_fail[0]):
        action=list_auth_ori[1]
    else:
        action='no_action'
            
     
	
    #src_u_domain = src_u_domain.split("$")[0]
    key=src_u_domain+'~'+src_com
    ind=dict_key.get(key,-1)
    #print('key: '+key+'  '+'ind: '+str(ind))
   
    
    error='1'
    if(action==list_auth_ori[0]) and ind !=-1:
        error='error1'
    elif(action==list_auth_ori[1]) and ind==-1:
        error='error2'
    elif 'SERVICE' in key:
        error='error3'
    elif(action==list_auth_ori[1]) and ind !=-1:
        write_auth_file(key,time)
        dict_key.pop(key)
        list_childdict[ind]=None
        set_key.remove(key)
        ind=dict_key.get(key,-1)
    elif ind != -1 and key in set_key and action not in list_auth_ori and auth_ori in list_auth_ori :
       
        child_dict=list_childdict[ind]
        dest_u_dom_cnt=child_dict.get('dest_u_dom_cnt',0)
        dest_u_dom_cnt=dest_u_dom_cnt+1
        child_dict['dest_u_dom_cnt']=dest_u_dom_cnt
            
        dest_com_cnt=child_dict.get('dest_com_cnt',0)
        dest_com_cnt=dest_com_cnt+1
        child_dict['dest_com_cnt']=dest_com_cnt
            
        auth_type='auth_'+auth_type
        auth_type_ind=child_dict.get(auth_type,0)
        if auth_type_ind == 0:
            child_dict[auth_type]=1
        else:
            auth_type_cnt=child_dict[auth_type]
            auth_type_cnt=auth_type_cnt+1
            child_dict[auth_type]=auth_type_cnt
                          
        logon_type='log_'+logon_type        
        logon_type_ind=child_dict.get(logon_type,0)
        if logon_type_ind == 0:
            child_dict[logon_type]=1
        else:
            logon_type_cnt=child_dict[logon_type]
            logon_type_cnt=logon_type_cnt+1
            child_dict[logon_type]=logon_type_cnt
            
        auth_ori_ind=child_dict.get(auth_ori,0)
        if auth_ori_ind == 0:
            child_dict[auth_ori]=1
        else:
            auth_ori_cnt=child_dict[auth_ori]
            auth_ori_cnt=auth_ori_cnt+1
            child_dict[logon_type]=auth_ori_cnt
                          
        suc_fail_ind=child_dict.get(suc_fail,0)
        if suc_fail_ind == 0:
            child_dict[suc_fail]=1
        else:
            suc_fail_cnt=child_dict[suc_fail]
            suc_fail_cnt=suc_fail_cnt+1
            child_dict[suc_fail]=suc_fail_cnt
        
        #unique set of dest
        un_dest=child_dict['un_dest']
        un_dest=un_dest+','+dest_com
        child_dict['un_dest']=un_dest
                  
        un_dest_dom=child_dict['un_dest_dom']
        un_dest_dom=un_dest_dom+','+dest_u_domain
        child_dict['un_dest_dom']=un_dest_dom
        
    elif ind == -1 and key not in set_key and action not in list_auth_ori and auth_ori in list_auth_ori :
       set_key.add(key)
       child_dict={}
       child_dict['start_time'] =time
       child_dict['dest_u_dom_cnt']=1
       child_dict['dest_com_cnt']=1
       auth_type='auth_'+auth_type
       child_dict[auth_type]=1
       logon_type='log_'+logon_type  
       child_dict[logon_type]=1
       child_dict[auth_ori]=1
       child_dict[suc_fail]=1
       #unique dest and dest_user_domain
       child_dict['un_dest']=''
       child_dict['un_dest_dom']=''
                
       list_childdict.append(child_dict)
       new_indx=len(list_childdict)-1
       dict_key[key]=new_indx
      
        
            
    if (action==list_auth_ori[0]) and ind ==-1 and 'SERVICE' not in key:
      set_key.add(key)
      child_dict={}
      child_dict['start_time'] =time
      child_dict['dest_u_dom_cnt']=1
      child_dict['dest_com_cnt']=1
      auth_type='auth_'+auth_type
      child_dict[auth_type]=1
      logon_type='log_'+logon_type  
      child_dict[logon_type]=1
      child_dict[auth_ori]=1
      child_dict[suc_fail]=1
      #unique dest and dest_user_domain
      child_dict['un_dest']=''
      child_dict['un_dest_dom']=''
                
      list_childdict.append(child_dict)
      new_indx=len(list_childdict)-1
      dict_key[key]=new_indx
            
            #set_auth_type.add(auth_type)
            #set_net_type(logon_type)
            #print('new_indx:' + str(new_indx))
                      
    

#main input and output files

filename_rd_auth='F:/dataset_AI/auth.txt'
#filename_rd_auth='F:/dataset_AI/splitted_files/auth_orignal.txt'
#filename_rd_proc='F:/dataset_AI/proc.txt'
filename_rd_proc='F:/dataset_AI/splitted_files/proc_temp.txt'
#fect_auth_new
#filename_wr_auth='F:/dataset_AI/splitted_files/fect_auth_new.txt'
filename_wr_auth= 'E:/Fectures/feacture.txt'
filename_wr_proc='F:/dataset_AI/fect_proc.txt'

dict_key={}
list_childdict=[]
set_key=set()

proc_key={}
list_procchilddict=[]
list_prockey=[]
    
list_auth_type= ['MICROSOFT_AUTHENTICATION_PACKAGE_','Kerberos','MICROSOFT_AUTHENTICATION_PACK','?',
'MICROSOFT_AUTHENTICATION_PAC','MICROSOFT_AUTHENTICATION_PA','MICROSOFT_AUTHENTICATION_PACKAG',
'MICROSOFT_AUTHENTICATION_PACKAGE_V1','MICROSOFT_AUTHENTICATION_PACKAGE_V1_0','Negotiate','NTLM','MICROSOFT_AUTHENTICATION_PACKAGE']
list_net_type=['Interactive','Network','RemoteInteractive','?','Batch','NewCredentials','Unlock','Service','NetworkCleartext','CachedInteractive']

#set_auth_type=set()
#set_net_type=set()
set_proc= set()
#TGS, TGT
list_auth_ori=['LogOn','LogOff']
list_suc_fail=['Success','Fail']
list_st_end=['Start','End']

line_no=0

#F:/dataset_AI/splitted_files/auth_orignal.txt
print("Reading..")
# reading and wrinting into main files
file_wr_auth = open(filename_wr_auth,"w")
#file_wr_proc=open(filename_wr_proc,"w")

read_file (filename_rd_auth)
#file_wr.close()
file_wr_auth.close()
