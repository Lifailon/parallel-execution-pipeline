# Update authorized_keys

The pipeline works in two modes, first to get the list of users on the remote host (connect to ssh on password) and update the parameters, then rewrite all keys or add a new key to `authorized_keys` and return the parameters to default values.

![](/update-authorized_keys/img/get-users.jpg)

![](/update-authorized_keys/img/rewrite-key.jpg)