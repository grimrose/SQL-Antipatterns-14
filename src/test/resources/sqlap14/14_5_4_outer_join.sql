SELECT
        bp1.product_id
      , b1.data_reported as latest
      , b1.bug_id
from Bugs b1
inner join BugsProducts bp1 on b1.bug_id = bp1.bug_id
left outer join
(
  Bugs as b2 inner join BugsProducts as bp2
  on b2.bug_id = bp2.bug_id
)
on (
      bp1.product_id = bp2.product_id
  and (
          b1.data_reported < b2.data_reported
      or  b1.data_reported = b2.data_reported
      and b1.bug_id < b2.bug_id
  )
)
where b2.bug_id is null
