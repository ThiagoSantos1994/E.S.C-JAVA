DELETE FROM
    tbd_DetalheDespesasMensais
WHERE
   id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcela = :idParcela
   AND id_Funcionario = :idFuncionario