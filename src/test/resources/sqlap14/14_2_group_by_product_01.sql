SELECT
        product_id
      , MAX(data_reported) as latest
from Bugs inner join BugsProducts using (bug_id)
GROUP by product_id
