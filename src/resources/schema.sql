if object_id('sync_sign') is  null

create table sync_sign(
  id int unique ,
  sign_consume int default 0
);
if object_id('TimeRecords_Pic') is  null

CREATE TABLE [dbo].[TimeRecords_Pic] (
[id] int NOT NULL IDENTITY(1,1) ,
[emp_id] nvarchar(32) COLLATE Chinese_PRC_CI_AS NULL ,
[sign_time] datetime NOT NULL ,
[flag] tinyint NULL DEFAULT ((0)) ,
[pic_local] varchar(100) COLLATE Chinese_PRC_CI_AS NULL ,
[pic_extranet] varchar(100) COLLATE Chinese_PRC_CI_AS NULL ,
CONSTRAINT [PK__TimeRecords_Pic__664BF223] PRIMARY KEY NONCLUSTERED ([id], [sign_time])
)



insert sync_sign (id) select 1 where not exists(select id from sync_sign where id=1);

insert sync_sign (id) select 2 where not exists(select id from sync_sign where id=2);
