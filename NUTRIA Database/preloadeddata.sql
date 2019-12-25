insert into action (name) value("action 1");
insert into action (name) value("action 2");
insert into action (name) value("action 3");
insert into action (name) value("action 4");
insert into action (name) value("action 4");



insert into certificate (name) value("certificate 1");
insert into certificate (name) value("certificate 2");
insert into certificate (name) value("certificate 3");
insert into certificate (name) value("certificate 4");
insert into certificate (name) value("certificate 5");


insert into producer (ethereum_account, licence_number, name, url) values("account 1", "licence 1", "producer 1", "url 1");
insert into producer (ethereum_account, licence_number, name, url) values("account 2", "licence 2", "producer 2", "url 2");
insert into producer (ethereum_account, licence_number, name, url) values("account 3", "licence 3", "producer 3", "url 3");
insert into producer (ethereum_account, licence_number, name, url) values("account 4", "licence 4", "producer 4", "url 4");
insert into producer (ethereum_account, licence_number, name, url) values("account 5", "licence 5", "producer 5", "url 5");


insert into check_point (date, latitude, longitude, qr_code, producer_id) 
					values(now(), 1234567, 76536321, "fldsjfljsdalfjsda", 1);
                    
insert into check_point (date, latitude, longitude, qr_code, producer_id) 
					values(now(), 5435324, 523453425, "gdfhfdghdsgfdsg", 2);
                    
insert into check_point (date, latitude, longitude, qr_code, producer_id) 
					values(now(), 5435324, 54325234, "gfdgdsfgdfgsdfghju", 3);

insert into check_point_action (check_point_id, action_id) values(2, 2);
insert into check_point_action (check_point_id, action_id) values(3, 3);
insert into check_point_action (check_point_id, action_id) values(2, 4);
insert into check_point_action (check_point_id, action_id) values(3, 4);
insert into check_point_action (check_point_id, action_id) values(3, 1);

insert into producer_action (producer_id, action_id) values(1, 2);
insert into producer_action (producer_id, action_id) values(2, 3);
insert into producer_action (producer_id, action_id) values(3, 3);
insert into producer_action (producer_id, action_id) values(3, 2);
insert into producer_action (producer_id, action_id) values(4, 1);
insert into producer_action (producer_id, action_id) values(4, 4);

insert into producer_certificate (producer_id, certificate_id) values(1, 1);
insert into producer_certificate (producer_id, certificate_id) values(2, 2);
insert into producer_certificate (producer_id, certificate_id) values(3, 3);
insert into producer_certificate (producer_id, certificate_id) values(4, 2);
insert into producer_certificate (producer_id, certificate_id) values(5, 1);



