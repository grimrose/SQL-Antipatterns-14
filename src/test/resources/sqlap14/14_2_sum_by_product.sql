SELECT
        product_id
      , sum(hours) as total_project_estimate
      , bug_id
from Bugs inner join BugsProducts using (bug_id)
GROUP by product_id
