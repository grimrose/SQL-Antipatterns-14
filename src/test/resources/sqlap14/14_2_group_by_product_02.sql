SELECT
        product_id
      , MAX(data_reported) as latest
      , bug_id
from Bugs inner join BugsProducts using (bug_id)
GROUP by product_id
