package com.datamantra.spark

import com.datamantra.spark.jobs.FraudDetectionTraining._
import org.apache.spark.ml.clustering.{KMeansModel, KMeans}
import org.apache.spark.ml.linalg.SQLDataTypes._
import org.apache.spark.sql.{SparkSession, Row, DataFrame}
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

/**
 * Created by kafka on 25/5/18.
 */
object DataBalancing {

  /*
  There will be more non-fruad transaction then fraund transaction. So non-fraud transactions must be balanced
  Kmeans Algorithm is used to balance non fraud transatiion.
  No. of non-fruad transactions  are balanced(reduced) to no. of fraud transaction
   */
  def createBalancedDataframe(df:DataFrame, reductionCount:Int)(implicit sparkSession:SparkSession) = {

    val kMeans = new KMeans().setK(reductionCount).setMaxIter(30)
    val kMeansModel = kMeans.fit(df)

    import sparkSession.implicits._
    kMeansModel.clusterCenters.toList.map(v => (v, 0)).toDF("features", "label")
    /*
    val featureSchema = StructType(
      Array(
        StructField("features", VectorType, true),
        StructField("label", IntegerType, true)
      ))
    val rowList = kMeansModel.clusterCenters.toList.map(v => Row(v, 0))
    val rowRdd = sparkSession.sparkContext.makeRDD(rowList)
    sparkSession.createDataFrame(rowRdd, featureSchema)
    */
  }
}
