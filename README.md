# cl-auto-renewer
Automatically renews Craigslist posts

This simple project helps you auto-renew your Craigslist posts because they expire so quickly. It uses Selenium to navigate the page and commons-email to send you a report after it runs. This service can be packaged up as a jar and triggered to run via cron or Windows scheduled tasks.

All active posts that can be renewed will be. Currently there isn't a way to choose which ones to auto-renew.

Required libraries:

Selenium 2.48+

Commons-email 1.4+
