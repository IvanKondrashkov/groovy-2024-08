package ru.otus.homework

import javax.sql.DataSource
import org.flywaydb.core.Flyway
import ru.otus.homework.model.Client
import ru.otus.homework.model.Manager
import ru.otus.homework.repository.DataTemplate
import ru.otus.homework.repository.DataTemplateJdbc
import ru.otus.homework.repository.DbExecutorImpl
import ru.otus.homework.service.DbServiceClientImpl
import ru.otus.homework.service.DbServiceManagerImpl
import ru.otus.homework.mapper.EntitySQLMetaDataImpl
import ru.otus.homework.mapper.EntityClassMetaDataImpl
import ru.otus.homework.datasource.DriverManagerDataSource
import ru.otus.homework.sessionmanager.TransactionRunnerJdbc

static void main(String[] args) {
  Properties properties = new Properties()
  ClassLoader classLoader = getClass().getClassLoader()
  File file = new File(classLoader.getResource("application.properties").getFile())
  file.withDataInputStream {
    properties.load(it)
  }

  final String URL = properties."datasource.url"
  final String USER = properties."datasource.username"
  final String PASSWORD = properties."datasource.password"
  final String DRIVER = properties."datasource.driver-class-name"

  def dataSource = new DriverManagerDataSource(URL, USER, PASSWORD, DRIVER)
  flywayMigrations(dataSource)
  def transactionRunner = new TransactionRunnerJdbc(dataSource)
  def dbExecutor = new DbExecutorImpl()

  def entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class)
  def entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient)
  def dataTemplateClient = new DataTemplateJdbc<>(
          dbExecutor: dbExecutor,
          entitySQLMetaData: entitySQLMetaDataClient,
          entityClassMetaData: entityClassMetaDataClient
  )

  def dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient as DataTemplate<Client>);
  def clientAfterSave = dbServiceClient.saveClient(new Client(name: "dbServiceFirst"))
  def clientSecond = dbServiceClient.saveClient(new Client(name: "dbServiceSecond"))
  def clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
  def clientForUpdate = clientSecondSelected
  clientForUpdate?.setName("New name for client")
  def clientAfterUpdate = dbServiceClient.saveClient(clientForUpdate)

  def entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class)
  def entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager)
  def dataTemplateManager = new DataTemplateJdbc<>(
          dbExecutor: dbExecutor,
          entitySQLMetaData: entitySQLMetaDataManager,
          entityClassMetaData: entityClassMetaDataManager
  )

  def dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager as DataTemplate<Manager>)
  def managerAfterSave = dbServiceManager.saveManager(new Manager(label: "ManagerFirst", param1: "param1"))
  def managerSecond = dbServiceManager.saveManager(new Manager(label: "ManagerSecond", param1: "param2"))
  def managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
  def managerForUpdate = managerSecondSelected
  managerForUpdate?.setLabel("New Label for manager")
  managerForUpdate?.setParam1("New param for manager")
  def managerAfterUpdate = dbServiceManager.saveManager(managerForUpdate)
}

private static void flywayMigrations(DataSource dataSource) {
  def flyway = Flyway.configure()
          .dataSource(dataSource)
          .baselineVersion('0')
          .baselineOnMigrate(true)
          .locations("classpath:/db/migration")
          .load()
  flyway.migrate()
}