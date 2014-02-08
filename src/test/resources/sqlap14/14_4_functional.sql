SELECT
        b.reported_by
      , a.account_name
from Bugs b inner join Accounts a ON b.reported_by = a.account_id
GROUP by b.reported_by
