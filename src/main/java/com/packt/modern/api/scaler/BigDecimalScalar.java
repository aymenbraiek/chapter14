package com.packt.modern.api.scaler;

import com.netflix.graphql.dgs.DgsScalar;

/**
 * I can't extend GraphqlBigDecimalCoercing
 */

@DgsScalar(name = "BigDecimal")
public class BigDecimalScalar {
}
