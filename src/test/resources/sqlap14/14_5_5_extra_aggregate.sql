select
        product_id
      , max(data_reported) as latest
      , max(bug_id) as latest_bug_id
from Bugs inner join BugsProducts using (bug_id)
group by product_id
