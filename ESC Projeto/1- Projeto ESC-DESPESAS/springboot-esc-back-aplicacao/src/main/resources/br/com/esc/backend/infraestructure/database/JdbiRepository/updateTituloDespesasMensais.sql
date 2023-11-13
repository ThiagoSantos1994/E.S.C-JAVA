UPDATE
    tbd_DespesaMensal
SET
    ds_NomeDespesa = UPPER(:dsNomeDespesa)
WHERE
   id_DetalheDespesa = :idDetalheDespesa
   AND id_Funcionario = :idFuncionario
   AND id_Despesa IN (SELECT DISTINCT(id_Despesa) from tbd_DespesasFixasMensais WHERE id_Funcionario = :idFuncionario AND ds_Ano = :anoReferencia)
