server {
    listen 8099 default_server;

    include /etc/nginx/includes/server_params.conf;
    include /etc/nginx/includes/proxy_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass http://backend;
        absolute_redirect off;
        proxy_redirect /sabnzbd/ {{ .entry }}/sabnzbd/;
        proxy_redirect /sabnzbd/wizard/ {{ .entry }}/sabnzbd/wizard/;

        sub_filter_once off;
        sub_filter_types *;
        sub_filter /sabnzbd {{ .entry }}/sabnzbd;
    }
}
