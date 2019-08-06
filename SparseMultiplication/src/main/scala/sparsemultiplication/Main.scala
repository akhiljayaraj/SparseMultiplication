package sparsemultiplication

object Main {
  import org.apache.spark.{SparkConf, SparkContext}
  import org.apache.spark.mllib.linalg.distributed.{CoordinateMatrix, MatrixEntry}
  import org.apache.spark.Partitioner


  class LeftMatrixPartitioner(override val numPartitions: Int) extends Partitioner {
    override def getPartition(key: Any): Int = {
      key match {
        case Long =>  key.hashCode()% numPartitions

        //This should not happen as only the key of the RDDs are passed to the getPartition method
        case  (j, (i, v)) => j.hashCode()% numPartitions
      }
    }
  }

  class RightMatrixPartitioner(override val numPartitions: Int) extends Partitioner {
    override def getPartition(key: Any): Int =
      key match {
        case Long => key.hashCode()% numPartitions

        //This should not happen as only the key of the RDDs are passed to the getPartition method
        case (j, (k, w)) => j.hashCode()% numPartitions
      }
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("First App")

    val sc = new SparkContext(conf)

    val mat: CoordinateMatrix = getMatrix("Matrix1.txt", sc)
    val mat2: CoordinateMatrix = getMatrix("Matrix2.txt", sc)

    val result: CoordinateMatrix = coordinateMatrixMultiply(mat, mat2)
    result.entries.collect().foreach(println)
  }

  def getMatrix(fileName:String, sc: SparkContext) : CoordinateMatrix = {

    val f = sc.textFile(fileName)

    val data = f.map(_.split(',') match {
      case Array(row, col, value) =>
        MatrixEntry(row.toInt, col.toInt, value.toDouble)
    }
    )
    new CoordinateMatrix(data)
  }

  def coordinateMatrixMultiply(leftMatrix: CoordinateMatrix,
                               rightMatrix: CoordinateMatrix): CoordinateMatrix = {

    val M_ = leftMatrix.entries
      .map({ case MatrixEntry(i, j, v) => (j, (i, v)) })
    val N_ = rightMatrix.entries
      .map({ case MatrixEntry(j, k, w) => (j, (k, w)) })

    //val M_partitioned =  M_.partitionBy(new LeftMatrixPartitioner(2))
    //val N_partitioned = M_.partitionBy(new RightMatrixPartitioner(2))

    val productEntries = M_
      .join(N_)
      .map({ case (_, ((i, v), (k, w))) => ((i, k), (v * w)) })
      .reduceByKey(_ + _)
      .map({ case ((i, k), sum) => MatrixEntry(i, k, sum) })

    new CoordinateMatrix(productEntries)
  }
}

