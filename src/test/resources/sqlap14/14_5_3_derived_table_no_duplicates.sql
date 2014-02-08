SELECT
        m.product_id
      , m.latest
      , max(b1.bug_id) as latest_bug_id
from Bugs b1 inner join
(
  select
          product_id
        , MAX(data_reported) as latest
  from Bugs b2 inner join BugsProducts using (bug_id)
  group by product_id
) as m
on b1.data_reported = m.latest
group by m.product_id, m.latest
