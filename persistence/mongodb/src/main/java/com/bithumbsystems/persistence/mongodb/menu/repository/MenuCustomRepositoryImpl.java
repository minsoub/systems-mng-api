package com.bithumbsystems.persistence.mongodb.menu.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MenuCustomRepositoryImpl implements MenuCustomRepository {

//  private static final long MAX_DEPTH_SUPPORTED = 1000;
  private final ReactiveMongoTemplate reactiveMongoTemplate;

//  @Override
//  public Flux<?> findMenuListBySiteId(String siteId, Boolean isUse, Class<?> outputType) {
//    final Criteria bySiteId = new Criteria("site_id").is(siteId);
//    final MatchOperation matchStage = Aggregation.match((isUse == null) ? bySiteId : bySiteId.andOperator(new Criteria("is_use").is(isUse)));
//
//    GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
//        .from("menu")
//        .startWith("$")
//        .connectFrom("_id")
//        .connectTo("parents_menu_id")
//        .maxDepth(MAX_DEPTH_SUPPORTED)
//        .as("child_menu");
//
//    Aggregation aggregation = Aggregation.newAggregation(matchStage, graphLookupOperation);
//    return reactiveMongoTemplate.aggregate(aggregation, "menu", outputType);
//  }

  @Override
  public Flux<Menu> findMenuListBySiteId(String siteId, Boolean isUse, String parentMenuId) {
    var condition = new ArrayList<Criteria>();
    condition.add(where("site_id").is(siteId));
    if(isUse != null) condition.add(where("is_use").is(isUse));
    if(parentMenuId == null || "".equals(parentMenuId)) {
      condition.add(where("parents_menu_id").is(""));  // .isNull());
    } else if (parentMenuId.isEmpty()) {
      condition.add(where("parents_menu_id").regex(".*" + parentMenuId + ".*"));
    } else {
      condition.add(where("parents_menu_id").is(parentMenuId));
    }
    var where = query(new Criteria().andOperator(condition));

    return reactiveMongoTemplate
        .find(where, Menu.class);
  }

}
