SELECT
        m.product_id
      , m.latest
      , b1.bug_id
from Bugs b1 inner join BugsProducts bp1 using (bug_id)
inner join
(
  select
          bp2.product_id
        , MAX(b2.data_reported) as latest
  from Bugs b2 inner join BugsProducts bp2 using (bug_id)
  group by bp2.product_id
) as m
on bp1.product_id = m.product_id
and b1.data_reported = m.latest
