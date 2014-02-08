select
        bp1.product_id
      , b1.data_reported as latest
      , b1.bug_id
from Bugs b1 inner join BugsProducts bp1 using (bug_id)
where not exists
(
  select *
  from Bugs b2 inner join BugsProducts bp2 using (bug_id)
  where
      bp1.product_id = bp2.product_id
  and b1.data_reported < b2.data_reported
)
