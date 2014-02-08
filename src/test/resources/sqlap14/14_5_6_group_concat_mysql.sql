select
        product_id
      , max(data_reported) as latest
      , group_concat(bug_id) as bug_id_list
from Bugs inner join BugsProducts using (bug_id)
group by product_id
