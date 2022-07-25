# CS-18000 Project5

### Commands

#### Account Commands (TO -> SERVER)
signup (String)username (String)name (int)age (String)password
login (String)username (String)password

edit_name (String)new_name
edit_age (int)new_age
edit_password (String)username (String)old_password (String)new_password

signout

delete_account (String)username (String)password

#### Communication Commands (TO -> SERVER)

send_message (String)chat_id (String)message

edit_message (String)chat_id (String)message_id (String)new_message
remove_message (String)chat_id (String)message_id 

create_dm (String)other_username

create_group (String)group_name

add_user_to_group (String)group_id (String)other_username
remove_user_from_group (String)group_id (String)other_username

remove_chat (String)chat_id 

request_info (String)requested_username
    
### Errors (FROM -> SERVER)

!invalid_login 
!username_taken
!user_not_found
!chat_not_found
!invalid_permissions
!stream_failed
!invalid_command

### Listener Commands (FROM -> SERVER)

@ (User)user_info
$ (DirectMessage)dm
% (DirectMessage)group
^ (String)name (int)age

SYMBOL LIST:
@ -> (User) Fetch From Signup Or Login
$ -> (DirectMessage) Fetch From Any Updates To DirectMessages
% -> (Group) Fetch From Any Updates To Groups
^ -> (String) (int) Fetch From Request User Info

